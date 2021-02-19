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
    boolean login() throws Exception;

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
