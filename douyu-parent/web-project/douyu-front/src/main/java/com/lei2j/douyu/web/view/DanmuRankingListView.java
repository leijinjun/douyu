package com.lei2j.douyu.web.view;

/**
 * author: lei2j
 * date: 2019/3/30
 */

public class DanmuRankingListView {

    private String roomId;

    private Long count;

    private String nickName;

    private String roomName;

    private String roomThumb;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DanmuRankingListView{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", count=").append(count);
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append(", roomThumb='").append(roomThumb).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
