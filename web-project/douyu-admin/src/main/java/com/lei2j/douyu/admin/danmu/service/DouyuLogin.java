package com.lei2j.douyu.admin.danmu.service;

import com.lei2j.douyu.vo.RoomGiftVo;

import java.io.IOException;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/28.
 */
public interface  DouyuLogin {

    /**
     * 登录
     * @throws IOException IOException
     * @return false is connect failed,true is connect success
     */
    boolean login()throws IOException;

    /**
     * 退出房间
     */
    void logout();

    /**
     * 获取房间号
     */
    Integer getRoom();

    /**
     * 重连
     */
    void retry();

    Map<Integer, RoomGiftVo> getRoomGift();
    
}
