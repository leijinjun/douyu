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
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * author: 98611
 * date: 2019/10/5
 */

@Configuration
public class DouyuWorker {

    private DouyuNioWorkEventLoop workEventLoop = new DouyuNioWorkEventLoop();

    @Resource
    private CacheRoomService cacheRoomService;

    public DouyuWorker() throws IOException {
    }

    /**
     * @param room  房间ID
     * @return 1 room login success,-1 room login fail
     * @throws IOException login error
     */
    public int login(Integer room) throws IOException {
        DouyuNioLogin douyuNioLogin = workEventLoop.get(room);
        if (douyuNioLogin.login()) {
            cacheRoomService.cache(room, douyuNioLogin);
            return 1;
        }
        return -1;
    }
}
