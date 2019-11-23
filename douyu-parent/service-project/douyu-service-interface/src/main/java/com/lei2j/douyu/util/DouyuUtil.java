package com.lei2j.douyu.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.vo.CateVo;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
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
        Elements lis = Jsoup.connect(DouyuApi.LIVE_CATE_ALL).get().body().getElementById("allCate").child(0)
                .select("[class*=categoryBox]:gt(1)")
                .select("ul[class='layout-Classify-list'] > li");
        List<CateVo> cateVos = new LinkedList<>();
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

    public static Optional<List<SearchRoomInfo>> search(String keyword){
        try {
            String str = HttpUtil.get("https://www.douyu.com/japi/search/api/getSearchRec?kw=" + keyword);
            JSONObject jsonObject = JSONObject.parseObject(str);
            int success = 0;
            int errorValue = jsonObject.getIntValue("error");
            if (errorValue == success) {
                JSONArray roomResult = jsonObject.getJSONObject("data").getJSONArray("roomResult");
                if (roomResult != null && !roomResult.isEmpty()) {
                    List<SearchRoomInfo> searchRoomInfoList = new ArrayList<>(5);
                    for (int i = 0; i < roomResult.size(); i++) {
                        JSONObject result = roomResult.getJSONObject(i);
                        SearchRoomInfo searchRoomInfo = JSONObject.toJavaObject(result, SearchRoomInfo.class);
                        searchRoomInfoList.add(searchRoomInfo);
                    }
                    return Optional.of(searchRoomInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static class SearchRoomInfo {

        /**
         * 房间图片
         */
        private String avatar;

        private String cateName;

        private Integer cid;

        /**
         * 是否开播，2=未开播
         */
        private Integer isLive;

        private String kw;

        private String nickName;

        private Integer rId;

        private Integer vipId;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public Integer getCid() {
            return cid;
        }

        public void setCid(Integer cid) {
            this.cid = cid;
        }

        public Integer getIsLive() {
            return isLive;
        }

        public void setIsLive(Integer isLive) {
            this.isLive = isLive;
        }

        public String getKw() {
            return kw;
        }

        public void setKw(String kw) {
            this.kw = kw;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Integer getRId() {
            return rId;
        }

        public void setRId(Integer rId) {
            this.rId = rId;
        }

        public Integer getVipId() {
            return vipId;
        }

        public void setVipId(Integer vipId) {
            this.vipId = vipId;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SearchRoomInfo{");
            sb.append("avatar='").append(avatar).append('\'');
            sb.append(", cateName='").append(cateName).append('\'');
            sb.append(", cid=").append(cid);
            sb.append(", isLive=").append(isLive);
            sb.append(", kw='").append(kw).append('\'');
            sb.append(", nickName='").append(nickName).append('\'');
            sb.append(", rId=").append(rId);
            sb.append(", vipId=").append(vipId);
            sb.append('}');
            return sb.toString();
        }
    }
}
