package com.lei2j.douyu.login.message.handler;

import com.lei2j.douyu.login.service.DouyuLogin;
import com.lei2j.douyu.login.service.MessageType;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lei2j
 */
@Component
public class RssHandler extends AbstractMessageHandler{

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ROOM_START)){
            HANDLER_MAP.put(MessageType.ROOM_START, this);
        }
    }

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        logger.info("房间|{},关闭直播",douyuLogin.getRoom());
        douyuLogin.logout();
    }
}
