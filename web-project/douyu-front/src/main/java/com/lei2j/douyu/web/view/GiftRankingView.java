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

package com.lei2j.douyu.web.view;

import java.math.BigDecimal;

/**
 * author: lei2j
 * date: 2019/3/30
 */

public class GiftRankingView {

    private String roomId;

    private BigDecimal giftMoney;

    private String nickName;

    private String roomName;

    private String roomThumb;

    private Integer roomStatus;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public BigDecimal getGiftMoney() {
        return giftMoney;
    }

    public void setGiftMoney(BigDecimal giftMoney) {
        this.giftMoney = giftMoney;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomThumb() {
        return roomThumb;
    }

    public void setRoomThumb(String roomThumb) {
        this.roomThumb = roomThumb;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GiftRankingView{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", giftMoney=").append(giftMoney);
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append(", roomThumb='").append(roomThumb).append('\'');
        sb.append(", roomStatus=").append(roomStatus);
        sb.append('}');
        return sb.toString();
    }
}
