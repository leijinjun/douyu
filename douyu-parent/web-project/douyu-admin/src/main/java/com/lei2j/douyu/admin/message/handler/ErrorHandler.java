package com.lei2j.douyu.admin.message.handler;

import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
@Component
public class ErrorHandler extends AbstractMessageHandler {

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ERROR)){
            HANDLER_MAP.put(MessageType.ERROR, this);
        }
    }

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        logger.error("房间|{},服务器返回错误",douyuLogin.getRoom());
        douyuLogin.retry();
    }
}
