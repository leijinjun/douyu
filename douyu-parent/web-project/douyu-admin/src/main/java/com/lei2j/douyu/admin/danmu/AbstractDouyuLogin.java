package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.service.DouyuKeepalive;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.admin.danmu.service.MessageDispatcher;
import com.lei2j.douyu.admin.danmu.handler.MessageHandler;
import com.lei2j.douyu.core.ApplicationContextUtil;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
 abstract class AbstractDouyuLogin implements DouyuLogin,MessageDispatcher {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDouyuLogin.class);

    /**
     * 心跳时间间隔，单位s
     */
    protected static final int INTERVAL_SECONDS = 43;

    /**
     * 斗鱼消息处理线程池
     */
    protected static ThreadPoolExecutor douyuMessageExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() + 1,
            Runtime.getRuntime().availableProcessors() * 2,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(100000),
            new DefaultThreadFactory("thd-douyu-message-handler-%d", true, 10),
            (runnable, threadPoolExecutor) -> LOGGER.warn("警告！！！，队列已满")
    );

    /**
     * 心跳检测线程池
     */
    protected static ScheduledExecutorService keepaliveScheduledExecutorService =
            new ScheduledThreadPoolExecutor(5, new DefaultThreadFactory("thd-douyu-keepalive-%d", true, 1));
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

    protected CacheRoomService cacheRoomService = ApplicationContextUtil.getBean(CacheRoomService.class);

    protected AbstractDouyuLogin() {
    }

    protected AbstractDouyuLogin(Integer room) {
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
            logger.debug("[DouyuLogin.dispatch]no match serialization handler,type:{}", type);
            return;
        }
        douyuMessageExecutor.execute(() -> {
            try {
                messageHandler.handle(dataMap, this);
            } catch (Exception e) {
                logger.error("保存消息失败", e.getCause());
            }
        });
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
    protected abstract DouyuDanmuLoginAuth getChatServerAddress(String username, String password) throws IOException;

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
        DouyuDanmuLoginAuth danmuLoginAuth = new DouyuDanmuLoginAuth(username, address);
        return danmuLoginAuth;
    }

    /**
     * 匿名登录弹幕服务器
     * @return DouyuDanmuLoginAuth
     */
    protected DouyuDanmuLoginAuth getChatServerAddress() throws IOException {
        return getChatServerAddress("","");
    }

    @Override
    public Integer getRoom() {
        return room;
    }

    public DouyuAddress getDouyuAddress() {
        return douyuAddress;
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
            final StringBuilder sb = new StringBuilder("DouyuDanmuLoginAuth{");
            sb.append("username='").append(username).append('\'');
            sb.append(", address=").append(address);
            sb.append('}');
            return sb.toString();
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
