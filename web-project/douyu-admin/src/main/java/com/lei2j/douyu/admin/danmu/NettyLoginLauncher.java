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
import com.lei2j.douyu.admin.cache.CacheRoomServiceImpl;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/10
 **/
@Component
public class NettyLoginLauncher {

    private CacheRoomService cacheRoomService;

    @Autowired
    public NettyLoginLauncher(CacheRoomService cacheRoomService) {
        this.cacheRoomService = cacheRoomService;
    }

    private EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("douyu-danmu-%d", false, 10));

    public int login(Integer room) throws Exception {
        DouyuLogin login = new DouyuNettyLogin(group, cacheRoomService, room);
        boolean success = login.login();
        if (success) {
            cacheRoomService.cache(room, login);
        }
        return success ? 1 : -1;
    }

    public static void main(String[] args) throws Exception {
        CacheRoomService cacheRoomService = new CacheRoomServiceImpl();
        NettyLoginLauncher nettyWorker = new NettyLoginLauncher(cacheRoomService);
        nettyWorker.login(4941977);
    }
}
