package com.lei2j.douyu.login.message.handler;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.es.search.ChatMessageIndex;
import com.lei2j.douyu.login.service.DouyuLogin;
import com.lei2j.douyu.login.service.MessageType;
import com.lei2j.douyu.vo.ChatMessageVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
@Component
public class ChatHandler extends AbstractMessageHandler{

    @Resource
    private ChatMessageIndex chatMessageIndex;
    
    protected ChatHandler() {
        super();
    }
    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        ChatMessageVo chatMessage = JSONObject.parseObject(JSONObject.toJSONString(messageMap), ChatMessageVo.class);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("+8"));
        chatMessage.setCreateAt(now);
        if(!StringUtils.isEmpty(chatMessage.getCid())){
            String cid = String.valueOf(chatMessage.getUid() + now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            chatMessage.setCid(cid);
        }
        chatMessageIndex.createDocument(String.valueOf(chatMessage.getCid()),chatMessage);
    }
    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.CHAT_MSG)){
            HANDLER_MAP.put(MessageType.CHAT_MSG, this);
        }
    }
    
}
