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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * author: 98611
 * date: 2019/10/5
 */

@Configuration
public class DouyuLoginLauncher {

    private DouyuNioWorkEventLoop workEventLoop;

    private CacheRoomService cacheRoomService;

    @Autowired
    public DouyuLoginLauncher(CacheRoomService cacheRoomService) throws IOException {
        this.cacheRoomService = cacheRoomService;
        workEventLoop = new DouyuNioWorkEventLoop(cacheRoomService);
    }

    /**
     * @param room  房间ID
     * @return 1 room login success,-1 room login fail
     * @throws IOException login error
     */
    public int login(Integer room) throws Exception {
        DouyuNioLogin douyuNioLogin = workEventLoop.get(room);
        if (douyuNioLogin.login()) {
            cacheRoomService.cache(room, douyuNioLogin);
            return 1;
        }
        return -1;
    }
}
