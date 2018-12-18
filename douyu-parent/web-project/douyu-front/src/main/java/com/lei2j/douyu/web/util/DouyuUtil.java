package com.lei2j.douyu.web.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lei2j
 */
public class DouyuUtil {

    public static RoomDetailVo getRoomDetail(Integer room){
        RoomDetailVo roomDetailVO ;
        String url = DouyuApi.ROOM_DETAIL_API.replace("{room}",String.valueOf(room));
        String jsonStr = HttpUtil.get(url,null);
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        //获取主播信息
        String errorKey = "error";
        if(jsonObj.getIntValue(errorKey)==0) {
            roomDetailVO = new RoomDetailVo();
            JSONObject dataObj = jsonObj.getJSONObject("data");
            roomDetailVO.setRoomId(dataObj.getInteger("room_id"));
            roomDetailVO.setRoomThumb(dataObj.getString("room_thumb"));
            roomDetailVO.setCateId(dataObj.getString("cate_id"));
            roomDetailVO.setCateName(dataObj.getString("cate_name"));
            roomDetailVO.setRoomName(dataObj.getString("room_name"));
            roomDetailVO.setRoomStatus(dataObj.getIntValue("room_status"));
            roomDetailVO.setOwnerName(dataObj.getString("owner_name"));
            roomDetailVO.setFansNum(dataObj.getInteger("fans_num"));
            roomDetailVO.setHn(dataObj.getInteger("hn"));
            JSONArray giftArr = dataObj.getJSONArray("gift");
            List<RoomGiftVo> roomGiftVOS = giftArr.toJavaList(RoomGiftVo.class);
            roomGiftVOS = roomGiftVOS.parallelStream().filter((var) -> {
                if (var.getType() == 1) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            roomDetailVO.setRoomGifts(roomGiftVOS);
            return roomDetailVO;
        }
        throw new RuntimeException("获取主播房间"+room+"礼物列表失败,请检查该房间！");
    }


}
