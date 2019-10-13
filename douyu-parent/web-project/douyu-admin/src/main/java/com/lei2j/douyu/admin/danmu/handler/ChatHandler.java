package com.lei2j.douyu.admin.danmu.handler;

import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.service.impl.es.ChatMessageIndex;
import com.lei2j.douyu.vo.ChatMessageVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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

    @Value("${douyu.chat.filter.min.level}")
    private int minLevel;

    @Resource
    private ChatMessageIndex chatMessageIndex;

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.CHAT_MSG)){
            HANDLER_MAP.put(MessageType.CHAT_MSG, this);
        }
    }

    @Override
    public void handle(Map<String, Object> messageMap, DouyuLogin douyuLogin) throws InvocationTargetException, IllegalAccessException {
        ChatMessageVo chatMessage = new ChatMessageVo();
        BeanUtils.populate(chatMessage, messageMap);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("+8"));
        chatMessage.setCreateAt(now);
        if(!StringUtils.isEmpty(chatMessage.getCid())){
            String cid = String.valueOf(chatMessage.getUid() + now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            chatMessage.setCid(cid);
        }
        if (filter(chatMessage)) {
            logger.debug("[ChatHandle.handler]弹幕消息被丢弃，room:{},serialization:{}", chatMessage.getRid(), chatMessage);
            return;
        }
        chatMessageIndex.createDocument(String.valueOf(chatMessage.getCid()),chatMessage);
    }

    private boolean filter(ChatMessageVo chatMessage) {
        Integer level = chatMessage.getLevel();
        return level == null || level <= minLevel;
    }
}
