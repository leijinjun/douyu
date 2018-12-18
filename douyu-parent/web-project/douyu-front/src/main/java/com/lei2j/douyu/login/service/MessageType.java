package com.lei2j.douyu.login.service;

/**
 * 消息类型
 * Created by lei2j on 2018/5/27.
 */
public interface MessageType {

    /**
     * 登录请求
     */
    String LOGIN = "loginreq";

    /**
     * 维持心跳
     */
    String KEEPLIVE = "mrkl";

    /**
     * 房间分组
     */
    String JOIN_GROUP = "joingroup";

    /**
     * 登出
     */
    String LOGOUT = "logout";

    /**
     * 弹幕
     */
    String CHAT_MSG = "chatmsg";

    /**
     * 赠送礼物
     */
    String GIVE_GIFT = "dgb";

    /**
     * 用户进房通知
     */
    String USER_ENTER = "uenter";

    /**
     * 房间开播
     */
    String ROOM_START = "rss";

    /**
     * 广播排行榜
     */
    String BROADCAST_LIST = "ranklist";

    /**
     * 超级弹幕
     */
    String SUPER_CHAT_MSG = "ssd";

    /**
     * 房间礼物广播
     */
    String ROOM_GIFT = "spbc";

    /**
     * 房间用户抢红包
     */
    String ROOM_GIFT_USER_GET = "ggbb";

    /**
     * 房间TOP10
     */
    String ROOM_RANK = "rankup";

    /**
     * 房间贵族列表
     */
    String ROOM_NOBLE_LIST = "online_noble_list";

    /**
     * 房间用户等级排行榜
     */
    String ROOM_USER_LIST = "ul_ranklist";

    /**
     * 粉丝排行榜
     */
    String ROOM_FANS_LSIT = "frank";

    String ERROR = "error";
}
