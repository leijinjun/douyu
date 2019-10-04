package com.lei2j.douyu.admin.web.controller;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.DouyuNioLogin;
import com.lei2j.douyu.admin.danmu.DouyuNormalLogin;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.response.ResponseCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/24.
 */
@RestController
@RequestMapping("/room/client")
public class DouyuLoginController extends BaseController{

    /**
     * 热度界限
     */
    private static final int MIN_HN = 1000000000;

    @Resource
    private CacheRoomService cacheRoomService;

    @PostMapping("/login/{room:\\d+}")
    public Response loginRoom(@PathVariable("room") Integer room) throws IOException{
        if (cacheRoomService.containsKey(room)) {
            logger.info("该房间{}已存在", room);
            return Response.newInstance(ResponseCode.ROOM_CONNECT_EXISTS);
        }
        RoomDetailVo roomDetailVO = DouyuUtil.getRoomDetail(room);
        Integer hn = roomDetailVO.getHn();
        DouyuLogin douyuLogin;
        if(hn>=MIN_HN){
            douyuLogin = new DouyuNormalLogin(room);
        }else{
            douyuLogin = new DouyuNioLogin(room);
        }
        if (douyuLogin.login() == -1) {
            return Response.newInstance(ResponseCode.ROOM_CONNECT_ERROR);
        }
        cacheRoomService.cache(room,douyuLogin);
        return Response.ok();
    }
    
    @PostMapping(value = "/logout")
    public Response logoutRoom(@RequestParam("room") Integer room) {
        DouyuLogin login = cacheRoomService.get(room);
        if (login != null) {
            logger.info("房间|{}，准备退出",room);
            login.logout();
        }
        return Response.ok();
    }
}
