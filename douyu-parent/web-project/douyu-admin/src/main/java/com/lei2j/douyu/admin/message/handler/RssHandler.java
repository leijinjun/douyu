package com.lei2j.douyu.admin.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.rholder.retry.*;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.HttpUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lei2j
 */
@Component
public class RssHandler extends AbstractMessageHandler{

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
            new DefaultThreadFactory("thd-rss-%d", false, 10));

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ROOM_START)){
            HANDLER_MAP.put(MessageType.ROOM_START, this);
        }
    }

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        scheduledExecutorService.submit(() -> {
            boolean closed = isClose(douyuLogin.getRoom());
            if (closed) {
                logger.info("房间|{},关闭直播", douyuLogin.getRoom());
                douyuLogin.logout();
            }
        });
    }

    private boolean isClose(Integer roomId){
        //重试3次，若每次都直播处于关闭状态，则退出关闭连接
        Retryer<Boolean> retry = RetryerBuilder.<Boolean>newBuilder()
                .retryIfException()
                .withWaitStrategy(WaitStrategies.incrementingWait(30, TimeUnit.SECONDS, 30, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .retryIfResult((result) -> Objects.equals(result, Boolean.TRUE))
                .build();
        Callable<Boolean> call = () -> {
            String roomUrl = DouyuApi.ROOM_DETAIL_API.replace("{room}", String.valueOf(roomId));
            String str = HttpUtil.get(roomUrl);
            JSONObject jsonObject = JSONObject.parseObject(str);
            String errorKey = "error";
            if (jsonObject.getIntValue(errorKey) == 0) {
                JSONObject dataObj = jsonObject.getJSONObject("data");
                // 未开播
                String closedRoomKey = "room_status";
                int closedRoomStatus = 2;
                return dataObj.getIntValue(closedRoomKey) != closedRoomStatus;
            }
            return false;
        };
        try {
            Boolean isClosed = retry.call(call);
            return isClosed;
        } catch (RetryException re) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
