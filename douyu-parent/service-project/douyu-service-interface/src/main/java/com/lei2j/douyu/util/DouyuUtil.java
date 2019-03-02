package com.lei2j.douyu.util;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.vo.CateVo;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lei2j
 */
public class DouyuUtil {

    /**
     * 获取直播间详细信息
     * @param room
     * @return
     */
    public static RoomDetailVo getRoomDetail(Integer room){
        String url = DouyuApi.ROOM_DETAIL_API.replace("{room}",String.valueOf(room));
        String jsonStr = HttpUtil.get(url,null);
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        //获取主播信息
        String errorKey = "error";
        if(jsonObj.getIntValue(errorKey)==0) {
            JSONObject dataObj = jsonObj.getJSONObject("data");
            RoomDetailVo roomDetailVo = dataObj.toJavaObject(RoomDetailVo.class);
            List<RoomGiftVo> roomGifts = roomDetailVo.getRoomGifts();
            roomGifts = roomGifts.parallelStream().filter((var) -> {
                if (var.getType() == 1) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
            roomDetailVo.setRoomGifts(roomGifts);
            return roomDetailVo;
        }
        throw new RuntimeException("获取主播房间"+room+"信息和礼物列表失败,请检查该房间！");
    }

    /**
     * 获取所有直播分类
     * @return
     * @throws IOException
     */
    public static List<CateVo> getAllCates() throws IOException {
        Elements lis = Jsoup.connect(DouyuApi.LIVE_CATE_ALL).get().body().getElementById("allCate").getElementsByTag("ul").get(0)
                .getElementsByTag("li");
        List<CateVo> cateVos = new LinkedList<>();
        System.out.println(lis.size());
        for (Element ele:
             lis) {
            CateVo cateVo = new CateVo();
            Element a = ele.getElementsByTag("a").get(0);
            String href = a.attr("href");
            String identityId = href.replaceAll("/g_","");
            String title = a.getElementsByTag("strong").get(0).text();
            cateVo.setCateName(title);
            cateVo.setCateId(identityId);
            cateVos.add(cateVo);
        }
        return cateVos;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getRoomDetail(485503));
    }
}
