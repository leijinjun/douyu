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

package com.lei2j.douyu.admin.danmu.netty;

import com.lei2j.douyu.admin.danmu.DouyuNettyLogin;
import com.lei2j.douyu.admin.danmu.handler.MessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/30
 **/
public class DouyuMessageHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DouyuMessageHandler.class);

    private DouyuNettyLogin login;

    public DouyuMessageHandler(DouyuNettyLogin login) {
        this.login = login;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) msg;
            String type = String.valueOf(map.get("type"));
            MessageHandler messageHandler = MessageHandler.HANDLER_MAP.get(type);
            if (messageHandler == null) {
                logger.debug("[DouyuLogin.dispatch]no match handler,type:{}", type);
            } else {
                messageHandler.handle(map, login);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
