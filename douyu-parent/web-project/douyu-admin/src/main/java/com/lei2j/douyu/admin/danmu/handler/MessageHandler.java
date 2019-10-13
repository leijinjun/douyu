package com.lei2j.douyu.admin.danmu.handler;

import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface MessageHandler {
	
	Map<String,MessageHandler> HANDLER_MAP = new HashMap<>();

    /**
     * 消息处理
     * @param messageMap messageMap
     * @param attachment attachment
     */
    void handle(Map<String, Object> messageMap, DouyuLogin attachment) throws Exception;
}
