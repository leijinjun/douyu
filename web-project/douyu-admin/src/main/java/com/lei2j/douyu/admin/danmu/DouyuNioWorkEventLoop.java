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

import java.io.IOException;
import java.util.*;

/**
 * author: 98611
 * date: 2019/10/4
 */

public class DouyuNioWorkEventLoop {

    private List<DouyuNioConnection> connectionList;

    private CacheRoomService cacheRoomService;

    public DouyuNioWorkEventLoop(CacheRoomService cacheRoomService) throws IOException {
        this(5);
        this.cacheRoomService = cacheRoomService;
    }

    public DouyuNioWorkEventLoop(int workerSize) throws IOException {
        int size = (int) (workerSize / 0.75) + 1;
        connectionList = new ArrayList<>(size);
        for (int i = 0; i < workerSize; i++) {
            connectionList.add(DouyuNioConnection.createConnection());
        }
    }

    public DouyuNioLogin get(Integer room) {
        Optional<DouyuNioConnection> optional = connectionList.stream().min(Comparator.comparing(DouyuNioConnection::getChannelCount));
        if (!optional.isPresent()) {
            throw new NullPointerException();
        }
        DouyuNioConnection douyuNioConnection = optional.get();
        DouyuNioLogin douyuNioLogin = new DouyuNioLogin(cacheRoomService, room, douyuNioConnection);
        return douyuNioLogin;
    }
}
