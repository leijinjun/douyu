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

import com.lei2j.douyu.admin.danmu.AbstractDouyuLogin.DouyuDanmuLoginAuth;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/19
 **/
public class LoginHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    private final static String TYPE_KEY = "type";
    private final static String LOGIN_RES = "loginres";
    private final static String MSG_IP_LIST = "msgiplist";

    private DouyuDanmuLoginAuth danmuLoginAuth = new DouyuDanmuLoginAuth();

    private ChannelPromise channelPromise;

    public LoginHandler(ChannelPromise channelPromise) {
        this.channelPromise = channelPromise;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) msg;
            if (Objects.equals(LOGIN_RES, map.get(TYPE_KEY))) {
                String username = String.valueOf(map.get("username"));
                danmuLoginAuth.setUsername(username);
                logger.info("[douyu.netty.login]登录响应(step2):[userName:{}]", username);
            } else if (Objects.equals(MSG_IP_LIST, map.get(TYPE_KEY))) {
                List<Map<String, String>> ipList = (List<Map<String, String>>) map.get("iplist");
                if (ipList == null) {
                    ipList = (List<Map<String, String>>) map.get("list");
                }
                Optional<Map<String, String>> optional = ipList.stream().findAny();
                if (!optional.isPresent()) {
                    throw new RuntimeException("ip list empty");
                }
                Map<String, String> ipMap = optional.get();
                String ip = String.valueOf(ipMap.get("ip"));
                int port = Integer.parseInt(ipMap.get("port"));
                danmuLoginAuth.setSocketAddress(ip, port);
                logger.info("[douyu.netty.login]登录响应(step3):[ip:{},port:{}]", ip, port);
                ctx.close(channelPromise);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
        logger.error("douyu login error", cause);
    }

    public DouyuDanmuLoginAuth getDanmuLoginAuth() {
        return danmuLoginAuth;
    }
}
