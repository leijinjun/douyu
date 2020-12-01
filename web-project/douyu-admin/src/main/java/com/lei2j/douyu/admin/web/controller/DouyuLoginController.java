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

package com.lei2j.douyu.admin.web.controller;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.DouyuLoginLauncher;
import com.lei2j.douyu.admin.danmu.NettyLoginLauncher;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.response.ResponseCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/24.
 */
@RestController
@RequestMapping("/room/client")
public class DouyuLoginController extends BaseController{

    @Resource
    private CacheRoomService cacheRoomService;
    @Resource
    private DouyuLoginLauncher douyuLoginLauncher;
    @Resource
    private NettyLoginLauncher nettyLoginLauncher;

    @PostMapping("/login/{room:\\d+}")
    public Response loginRoom(@PathVariable("room") Integer room) throws Exception {
        if (cacheRoomService.containsKey(room)) {
            logger.info("该房间{}已存在", room);
            return Response.newInstance(ResponseCode.ROOM_CONNECT_EXISTS);
        }
        if (nettyLoginLauncher.login(room) == -1) {
            return Response.newInstance(ResponseCode.ROOM_CONNECT_ERROR);
        }
        return Response.ok();
    }
    
    @PostMapping(value = "/logout")
    public Response logoutRoom(@RequestParam("room") Integer room) {
        DouyuLogin login = cacheRoomService.get(room);
        if (login != null) {
            logger.info("房间|{}，准备退出", room);
            login.logout();
            return Response.ok();
        } else {
            return Response.NOT_FOUND;
        }
    }
}
