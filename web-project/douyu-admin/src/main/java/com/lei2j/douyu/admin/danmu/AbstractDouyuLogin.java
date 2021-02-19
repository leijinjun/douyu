/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.service.DouyuKeepalive;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.admin.danmu.service.MessageDispatcher;
import com.lei2j.douyu.admin.danmu.handler.MessageHandler;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/26.
 */
 public abstract class AbstractDouyuLogin implements DouyuLogin,MessageDispatcher {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDouyuLogin.class);

    /**
     * 心跳时间间隔，单位s
     */
    protected static final int INTERVAL_SECONDS = 43;

    /**
     * 心跳检测线程池
     */
    protected static ScheduledExecutorService keepaliveScheduledExecutorService =
            new ScheduledThreadPoolExecutor(10, new DefaultThreadFactory("thd-douyu-keepalive-%d", true, 1));
    /**
     *房间礼物信息
     */
    protected Map<Integer, RoomGiftVo> roomGiftMap;

    /**
     * 房间ID
     */
    protected Integer room;

    protected DouyuAddress douyuAddress;

    /**
     * 房间心跳检测
     */
    protected KeepaliveSchedule keepaliveSchedule;

    protected CacheRoomService cacheRoomService;

    protected AbstractDouyuLogin(CacheRoomService cacheRoomService,Integer room) {
        this.cacheRoomService = cacheRoomService;
        this.room = room;
        RoomDetailVo roomDetailVo = DouyuUtil.getRoomDetail(room);
        this.roomGiftMap = roomDetailVo.getRoomGifts().stream().collect(Collectors.toMap(RoomGiftVo::getId, Function.identity()));
    }

    @Override
    public void dispatch(Map<String,Object> dataMap) {
        logger.debug("接收消息:{}", dataMap);
        String type = String.valueOf(dataMap.get("type"));
        MessageHandler messageHandler = MessageHandler.HANDLER_MAP.get(type);
        if (messageHandler == null) {
            logger.debug("[DouyuLogin.dispatch]no match handler,type:{}", type);
            return;
        }
        messageHandler.handle(dataMap, this);
    }

    @Override
    public Map<Integer, RoomGiftVo> getRoomGift() {
        return roomGiftMap;
    }

    /**
     * 获取弹幕服务器信息
     * @param username 登录用户名
     * @param password 登录密码
     * @return DouyuDanmuLoginAuth
     * @throws IOException 登录授权异常
     */
    protected abstract DouyuDanmuLoginAuth getChatServerAddress(String username, String password) throws Exception;

    @SuppressWarnings("unchecked")
    protected DouyuDanmuLoginAuth getLoginAuth(Map<String, Object> loginMessageMap,Map<String, Object> addressMap){
        logger.info("房间|{},登录响应信息:{}",room,loginMessageMap);
        String type = "type";
        String error = "error";
        if(error.equals(loginMessageMap.get(type))){
            logger.error("房间|{},登录失败,错误信息:{}",room,loginMessageMap);
            return null;
        }
        String username = String.valueOf(loginMessageMap.get("username"));
        List<Map<String,String>> ipList = (List<Map<String,String>>)addressMap.get("iplist");
        if(ipList==null) {
            ipList = (List<Map<String,String>>)addressMap.get("list");
        }
        Optional<Map<String, String>> optional = ipList.stream().findAny();
        if (!optional.isPresent()) {
            logger.info("房间|{}，获取弹幕服务器列表失败", room);
            return null;
        }
        Map<String, String> ipMap = optional.get();
        String ip = String.valueOf(ipMap.get("ip"));
        int port = Integer.parseInt(ipMap.get("port"));
        DouyuAddress address = new DouyuAddress(ip, port);
        return new DouyuDanmuLoginAuth(username, address);
    }

    /**
     * 匿名登录弹幕服务器
     * @return DouyuDanmuLoginAuth
     */
    protected DouyuDanmuLoginAuth getChatServerAddress() throws Exception {
        return getChatServerAddress("","");
    }

    @Override
    public Integer getRoom() {
        return room;
    }

    public DouyuAddress getDouyuAddress() {
        return douyuAddress;
    }

    public static class DouyuDanmuLoginAuth {

        private String username;

        private DouyuAddress address;

        private SocketAddress socketAddress;

        public DouyuDanmuLoginAuth() {
        }

        public DouyuDanmuLoginAuth(String username, DouyuAddress address) {
            this.username = username;
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public DouyuAddress getAddress() {
            return address;
        }

        public void setAddress(DouyuAddress address) {
            this.address = address;
        }

        public SocketAddress getSocketAddress() {
            return socketAddress;
        }

        public void setSocketAddress(String host,int port) {
            this.socketAddress = InetSocketAddress.createUnresolved(host, port);
        }

        @Override
        public String toString() {
            return "DouyuDanmuLoginAuth{" +
                    "username='" + username + '\'' +
                    ", address=" + address +
                    ", socketAddress=" + socketAddress +
                    '}';
        }
    }

    class KeepaliveSchedule {

        private ScheduledFuture<?> scheduledFuture;

        public KeepaliveSchedule(){
        }

        public void schedule(DouyuKeepalive douyuKeepalive) {
            scheduledFuture = keepaliveScheduledExecutorService.scheduleWithFixedDelay(
                    douyuKeepalive::keepalive,
                    INTERVAL_SECONDS,
                    INTERVAL_SECONDS,
                    TimeUnit.SECONDS);
        }

        public void cancel(){
            scheduledFuture.cancel(true);
        }
    }
}
