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

package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.config.DouyuMessageConfig;
import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.admin.danmu.netty.*;
import com.lei2j.douyu.core.config.DouyuAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;


/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/30
 **/
public class DouyuNettyLogin extends AbstractDouyuLogin {

    private EventLoopGroup group;

    private Channel channel;

    public DouyuNettyLogin(EventLoopGroup group, CacheRoomService cacheRoomService, Integer room) {
        super(cacheRoomService, room);
        this.group = group;
    }

    @Override
    protected DouyuDanmuLoginAuth getChatServerAddress(String username, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean login() throws Exception {
        DouyuAddress douyuAddress = DouyuMessageConfig.getLoginServerAddress(room);
        this.douyuAddress = douyuAddress;
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture future = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {

                    }
                })
                .connect(douyuAddress.getIp(), douyuAddress.getPort()).sync();
        ChannelPromise newPromise = future.channel().newPromise();
        LoginHandler loginHandler = new LoginHandler(newPromise);
        future.channel().pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024 * 1024, 0, 4, 0, 0, true))
                .addLast(new MessageParseHandler())
                .addLast(loginHandler)
                .addLast(new DoubleLengthFieldPrepender(4))
                .addLast(new MessageAssemblyHandler());
        DouyuMessage loginMessage = DouyuMessageConfig.getLoginMessage(getRoom(), "", "");
        logger.info("[douyu.netty.login]开始发送登录请求(step1)，登录消息:{}", loginMessage.getData());
        future.channel().writeAndFlush(loginMessage);
        DouyuDanmuLoginAuth danmuLoginAuth;
        boolean p = newPromise.addListener(ChannelFutureListener.CLOSE_ON_FAILURE).await(1, TimeUnit.MINUTES) && newPromise.isSuccess();
        boolean p1 = (danmuLoginAuth = loginHandler.getDanmuLoginAuth()) != null && danmuLoginAuth.getSocketAddress() != null;
        if (!(p && p1)) {
            logger.info("[douyu.netty.login]连接弹幕认证服务器失败|{}", getRoom());
            newPromise.channel().close();
            return false;
        }
        logger.info("[douyu.netty.login]弹幕服务器认证成功|{}", getRoom());
        return login0(danmuLoginAuth);
    }

    @Override
    public void logout() {
        synchronized (this){
            channel.pipeline().writeAndFlush(DouyuMessageConfig.getLogoutMessage()).addListener(ChannelFutureListener.CLOSE);
            cacheRoomService.remove(room);
            channel.close();
            logger.info("房间|{}，成功退出",room);
        }
    }

    @Override
    public void retry() {
        logger.info("重新连接房间|{}",room);
        try {
            channel.close();
            channel = null;
        } catch (Exception ignore) {
        }
        try {
            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChannel(Channel channel) {
        this.channel = channel;
    }

    private boolean login0(DouyuDanmuLoginAuth danmuLoginAuth) throws InterruptedException {
        Integer room = getRoom();
        logger.info("[douyu.netty.login]开始连接弹幕服务器|{}",room);
        DouyuNettyLogin login = this;
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(60, 40, 0, TimeUnit.SECONDS))
                                .addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 1024 * 1024, 0, 4, 0, 0, true))
                                .addLast(new MessageParseHandler())
                                .addLast(new DouyuMessageHandler(login))
                                .addLast(new DoubleLengthFieldPrepender(4))
                                .addLast(new MessageAssemblyHandler())
                                .addLast(new HeartbeatHandler(login));
                    }
                })
                .connect(danmuLoginAuth.getSocketAddress()).sync();
        Channel channel = channelFuture.channel();
        setChannel(channel);
        channel.pipeline().write(DouyuMessageConfig.getLoginMessage(room, danmuLoginAuth.getUsername(), "1234567890123456"));
        boolean success = channel.pipeline().writeAndFlush(DouyuMessageConfig.getJoinMessage(room)).sync().isSuccess();
        if (success) {
            logger.info("[douyu.netty.login]弹幕服务器连接成功|{}", room);
        }
        return success;
    }
}
