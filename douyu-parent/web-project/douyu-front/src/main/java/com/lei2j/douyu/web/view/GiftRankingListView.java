package com.lei2j.douyu.web.view;

import java.math.BigDecimal;

/**
 * author: lei2j
 * date: 2019/3/30
 */

public class GiftRankingListView {

    private String roomId;

    private BigDecimal giftMoney;

    private String nickName;

    private String roomName;

    private String roomThumb;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GiftRankingListView{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", giftMoney=").append(giftMoney);
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append(", roomThumb='").append(roomThumb).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
