package com.lei2j.douyu.core.constant;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface DouyuApi {

    /**
     * 具体房间详细信息接口
     */
    String  ROOM_DETAIL_API = "http://open.douyucdn.cn/api/RoomApi/room/{room}";

    /**
     * 获取所有房间信息接口(所有热门直播，无分类)
     */
    String ROOM_ALL_API = "https://www.douyu.com/gapi/rkc/directory/0_0/{page}";

    /**
     * 获取所有分类房间信息接口
     */
    String ROOM_CATE_API = "https://www.douyu.com/gapi/rkc/directory/2_{cateId}/{page}";

    /**
     * 房间弹幕连接服务器组
     */
    String ROOM_SERVER_CONFIG = "https://www.douyu.com/betard/{room}";
}
