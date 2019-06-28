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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lei2j
 */
public class DouyuUtil {

    /**
     * 获取直播间详细信息
     * @param room roomId
     * @return RoomDetailVo
     */
    public static RoomDetailVo getRoomDetail(Integer room){
        String url = DouyuApi.ROOM_DETAIL_API.replace("{room}",String.valueOf(room));
        RoomDetailVo roomDetailVo = null;
        try {
            String jsonStr = HttpUtil.get(url,null);
            JSONObject jsonObj = JSONObject.parseObject(jsonStr);
            //获取主播信息
            String errorKey = "error";
            if(jsonObj.getIntValue(errorKey)==0) {
                JSONObject dataObj = jsonObj.getJSONObject("data");
                roomDetailVo = dataObj.toJavaObject(RoomDetailVo.class);
                List<RoomGiftVo> roomGifts = roomDetailVo.getRoomGifts();
                roomGifts = roomGifts.parallelStream().filter(var -> var.getType() != 1).collect(Collectors.toList());
                roomDetailVo.setRoomGifts(roomGifts);
                return roomDetailVo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roomDetailVo;
    }

    /**
     * 获取所有直播分类
     * @return List
     * @throws IOException IOException
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

    public static Optional<SearchRoomInfo> search(String keyword){
        SearchRoomInfo searchRoomInfo = null;
        try {
            searchRoomInfo  = new SearchRoomInfo();
            Element body =
                    Jsoup.connect("https://www.douyu.com/search/").data("kw", keyword).header("user-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36").get().body();
            Element select = body.selectFirst("ul[class='anchor-list-box']");
            Element li = select.selectFirst("li");
            Element a = li.getElementsByTag("a").first();
            String rid = a.attr("data-rid");
            String img = a.getElementsByClass("anchor-avatar").first().getElementsByTag("img").first().attr("data-original");
            String ownerName = a.getElementsByClass("anchor-name").text();
            String fansNum = a.getElementsByClass("anchor-info").first().ownText().trim();
            searchRoomInfo.setRoomId(Integer.parseInt(rid));
            searchRoomInfo.setRoomThumb(img);
            searchRoomInfo.setOwnerName(ownerName);
            searchRoomInfo.setFansNum(Integer.parseInt(fansNum));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(searchRoomInfo);
    }

    public static class SearchRoomInfo {

        private Integer roomId;

        private String roomThumb;

        private String ownerName;

        private Integer fansNum;

        public Integer getRoomId() {
            return roomId;
        }

        public void setRoomId(Integer roomId) {
            this.roomId = roomId;
        }

        public String getRoomThumb() {
            return roomThumb;
        }

        public void setRoomThumb(String roomThumb) {
            this.roomThumb = roomThumb;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public Integer getFansNum() {
            return fansNum;
        }

        public void setFansNum(Integer fansNum) {
            this.fansNum = fansNum;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SearchRoomInfo{");
            sb.append("roomId=").append(roomId);
            sb.append(", roomThumb='").append(roomThumb).append('\'');
            sb.append(", ownerName='").append(ownerName).append('\'');
            sb.append(", fansNum=").append(fansNum);
            sb.append('}');
            return sb.toString();
        }
    }
}
