package com.lei2j.douyu.admin.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.util.BeanUtils;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomVo;
import com.lei2j.douyu.web.response.Response;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@RestController
@RequestMapping("/room/info")
public class RoomAdminController extends BaseController {

    @Resource
    private CacheRoomService cacheRoomService;

    /**
     * 获取直播列表
     * @param cateId 分类标识
     * @param limit limit
     * @return Response
     */
    @GetMapping("/list")
    public Response getRoomList(@RequestParam(value = "cate",required = false)String cateId,
                                @RequestParam(value = "limit",required = false,defaultValue = "100") Integer limit){
        LinkedList<RoomVo> list = new LinkedList<>();
        StringBuilder url = new StringBuilder(DouyuApi.ROOM_ALL_API);
        if(!StringUtils.isEmpty(cateId)){
            url.append("/").append(cateId);
        }
        url.append("?limit=").append(limit);
        String s = HttpUtil.get(url.toString());
        JSONObject var1 = JSONObject.parseObject(s);
        String codeKey = "error";
        if(var1.getIntValue(codeKey)==0){
            JSONArray jsonArray = var1.getJSONArray("data");
            for (Object jsonObject:
                 jsonArray) {
                    RoomVo roomVo = JSONObject.toJavaObject((JSONObject) jsonObject, RoomVo.class);
                    boolean f = cacheRoomService.containsKey(roomVo.getRoomId());
                    roomVo.setConnected(f);
                    if(f){
                        list.addFirst(roomVo);
                    }else {
                        list.add(roomVo);
                    }
            }
        }
        return Response.ok().entity(list);
    }

    /**
     * 获取所有直播分类列表
     * @return Response
     * @throws IOException IOException
     */
    @GetMapping("/cates")
    public Response getAllCate()throws IOException {
        return  Response.ok().entity(DouyuUtil.getAllCates());
    }

    /**
     * 获取已连接房间列表
     * @return Response
     */
    @GetMapping("/logged")
    public Response getLoggedRooms() throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        Map<Integer, DouyuLogin> map = cacheRoomService.getAll();
        Set<Map.Entry<Integer, DouyuLogin>> entrySet = map.entrySet();
        List<RoomVo> list = new ArrayList<>();
        for (Map.Entry<Integer, DouyuLogin> entry :
                entrySet) {
            DouyuLogin value = entry.getValue();
            RoomDetailVo roomDetail = DouyuUtil.getRoomDetail(entry.getKey());
            RoomVo roomVO = BeanUtils.copyProperties(roomDetail,RoomVo.class);
            roomVO.setConnected(true);
            roomVO.setNickname(roomDetail.getOwnerName());
            roomVO.setRoomSrc(roomDetail.getRoomThumb());
            list.add(roomVO);
        }
        return Response.ok().entity(list);
    }
}
