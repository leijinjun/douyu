package com.lei2j.douyu.admin.danmu.service;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.message.handler.MessageHandler;
import com.lei2j.douyu.core.ApplicationContextUtil;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.danmu.service.DouyuKeeplive;
import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.danmu.service.MessageDispatcher;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/26.
 */
public abstract class AbstractDouyuLogin implements DouyuLogin,MessageDispatcher {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 心跳时间间隔，单位s
     */
    protected static final int INTERVAL_SECONDS = 45;

    /**
     * 斗鱼消息处理线程池
     */
    protected static ThreadPoolExecutor douyuMessageExecutor = new ThreadPoolExecutor(Runtime.getRuntime()
            .availableProcessors()
            +1,5, 30, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000),
            new DefaultThreadFactory("Thread-douyu-message-%d", true, 10),(runnable,threadPoolExecutor)->{

    }
    );

    /**
     * 心跳检测线程池
     */
    protected static ScheduledExecutorService keepliveScheduledExecutorService = new ScheduledThreadPoolExecutor(5,
    		new DefaultThreadFactory("Thread-douyu-keeplive-%d", true, 10));

    /**
     *房间信息
     */
    protected RoomDetailVo roomDetail;

    /**
     * 房间ID
     */
    protected Integer room;

    protected DouyuAddress douyuAddress;

    /**
     * 房间心跳检测
     */
    protected KeepliveSchedule keepliveSchedule;

    protected CacheRoomService cacheRoomService = ApplicationContextUtil.getBean(CacheRoomService.class);

    public AbstractDouyuLogin() {
    }

    protected AbstractDouyuLogin(Integer room) throws IOException{
        this.room = room;
        init();
    }

    /**
     * 实现类自定义初始化
     * @throws IOException
     */
    protected abstract void init() throws IOException;

    @Override
    public void dispatch(DouyuMessage douyuMessage) {
        Map<String, Object> messageMap = MessageParse.parse(douyuMessage);
        logger.debug("接收消息:{}",messageMap);
        String type = String.valueOf(messageMap.get("type"));
        //礼物类型
        if (MessageType.GIVE_GIFT.equals(type)) {
            String gfid = String.valueOf(messageMap.get("gfid"));
            //收费礼物列表
            Map<Integer, RoomGiftVo> roomGift = getRoomGift();
            Integer var3 = Integer.valueOf(gfid);
            //过滤免费礼物
            if (!roomGift.containsKey(var3)) {
                return;
            }
            messageMap.put("pc", roomGift.get(var3).getPc());
            MessageHandler messageHandler = MessageHandler.HANDLER_MAP.get(type);
            if (messageHandler != null) {
                messageHandler.handler(messageMap,this);
            }
            return;
        }
        DouyuLogin douyuLogin = this;
        douyuMessageExecutor.execute(() -> {
            MessageHandler messageHandler = MessageHandler.HANDLER_MAP.get(type);
            if (messageHandler != null) {
                messageHandler.handler(messageMap, douyuLogin);
            }
        });
    }

    public Map<Integer,RoomGiftVo> getRoomGift(){
        Map<Integer,RoomGiftVo> dataMap = new HashMap<>(8);
        if (roomDetail != null) {
            List<RoomGiftVo> roomGifts = roomDetail.getRoomGifts();
            roomGifts.forEach((var) -> dataMap.put(var.getId(), var));
        }
        return dataMap;
    }

    /**
     * 获取弹幕服务器信息
     * @param username 登录用户名
     * @param password 登录密码
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
	protected DouyuDanmuLoginAuth getChatServerAddress(String username, String password) throws IOException{
        DouyuAddress douyuAddress = DouyuMessageConfig.getLoginServerAddress(room);
        DouyuConnection douyuConnection = DouyuConnection.initConnection(douyuAddress);
        douyuConnection.write(DouyuMessageConfig.getLoginMessage(room,username,password));
        Map<String, Object> loginMessageMap = MessageParse.parse(douyuConnection.read());
        Map<String, Object> msgIpList = MessageParse.parse(douyuConnection.read());
        douyuConnection.close();
        logger.info("房间|{},登录响应信息:{}",room,loginMessageMap);
        String type = "type";
        String error = "error";
        if(error.equals(loginMessageMap.get(type))){
            logger.error("房间|{},登录失败,错误信息:{}",room,loginMessageMap);
            return null;
        }
        username = String.valueOf(loginMessageMap.get("username"));
        List<Map<String,String>> ipList = (List<Map<String,String>>)msgIpList.get("iplist");
        if(ipList==null) {
        	ipList = (List<Map<String,String>>)msgIpList.get("list");
        }
        Map<String, String> ipMap = ipList.get(0);
        String ip = String.valueOf(ipMap.get("ip"));
        int port = Integer.parseInt(ipMap.get("port"));
        DouyuAddress address = new DouyuAddress(ip, port);
        DouyuDanmuLoginAuth danmuLoginAuth = new DouyuDanmuLoginAuth(username, address);
        return danmuLoginAuth;
    }

    /**
     * 匿名登录弹幕服务器
     * @return
     * @throws IOException
     */
    protected DouyuDanmuLoginAuth getChatServerAddress() throws IOException{
        return getChatServerAddress("","");
    }

    @Override
    public Integer getRoom() {
        return room;
    }

    public DouyuAddress getDouyuAddress() {
        return douyuAddress;
    }

    public void setDouyuAddress(DouyuAddress douyuAddress) {
        this.douyuAddress = douyuAddress;
    }

    class DouyuDanmuLoginAuth {

        private String username;

        private DouyuAddress address;

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

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("DouyuDanmuLoginAuth{");
            sb.append("username='").append(username).append('\'');
            sb.append(", address=").append(address);
            sb.append('}');
            return sb.toString();
        }
    }

    class KeepliveSchedule {

        private ScheduledFuture<?> scheduledFuture;

        public KeepliveSchedule(){
        }

        public void schedule(DouyuKeeplive douyuKeeplive) {
            scheduledFuture = keepliveScheduledExecutorService.scheduleWithFixedDelay(() -> douyuKeeplive.keeplive() , INTERVAL_SECONDS, INTERVAL_SECONDS, TimeUnit.SECONDS);
        }

        public void cancel(){
            scheduledFuture.cancel(false);
        }
    }
}
