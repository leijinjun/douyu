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

package com.lei2j.douyu.admin.danmu.handler;

import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.service.impl.es.ChatMessageIndex;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
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
import java.util.concurrent.*;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
@Component
public class ChatHandler extends AbstractMessageHandler{

    private final BlockingQueue<ChatMessageVo> queue = new ArrayBlockingQueue<>(50000);

    private volatile boolean isExecuted;

    private ExecutorService danmuMessageExecutor = new ThreadPoolExecutor(1, 1, 30, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(1), new DefaultThreadFactory("thd-chat-handler-%d", true, 10));

    @Value("${douyu.chat.filter.min.level}")
    private int minLevel;

    @Resource
    private ChatMessageIndex chatMessageIndex;

    @Override
    protected void afterSetHandler() {
        if (!HANDLER_MAP.containsKey(MessageType.CHAT_MSG)) {
            HANDLER_MAP.put(MessageType.CHAT_MSG, this);
        }
    }

    private void executeLoop() {
        danmuMessageExecutor.submit(() -> {
            for (; ; ) {
                try {
                    ChatMessageVo chatMessage = queue.poll(1, TimeUnit.MINUTES);
                    if (chatMessage == null) {
                        return;
                    }
                    chatMessageIndex.createDocument(String.valueOf(chatMessage.getCid()), chatMessage);
                } catch (Throwable e) {
                    logger.error("[douyu.chat]处理消息异常", e);
                }
            }
        });
    }

    private void start() {
        if (!isExecuted) {
            synchronized (this) {
                if (!isExecuted) {
                    isExecuted = true;
                    executeLoop();
                }
            }
        }
    }

    @Override
    public void handle(Map<String, Object> messageMap, DouyuLogin douyuLogin){
        start();
        ChatMessageVo chatMessage = new ChatMessageVo();
        try {
            BeanUtils.populate(chatMessage, messageMap);
            LocalDateTime now = LocalDateTime.now(ZoneId.of("+8"));
            chatMessage.setCreateAt(now);
            if (!StringUtils.isEmpty(chatMessage.getCid())) {
                String cid = String.valueOf(chatMessage.getUid() + now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                chatMessage.setCid(cid);
            }
            if (filter(chatMessage)) {
                logger.debug("[ChatHandle.handler]弹幕消息被丢弃，room:{},serialization:{}", chatMessage.getRid(), chatMessage);
                return;
            }
            boolean isAdded = queue.offer(chatMessage);
            if (!isAdded) {
                chatMessageIndex.createDocument(String.valueOf(chatMessage.getCid()), chatMessage);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean filter(ChatMessageVo chatMessage) {
        Integer level = chatMessage.getLevel();
        return level == null || level <= minLevel;
    }
}
