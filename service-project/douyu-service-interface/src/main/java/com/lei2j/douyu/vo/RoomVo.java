package com.lei2j.douyu.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author lei2j
 */
public class RoomVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "room_id")
    private Integer roomId;

    @JSONField(name="room_src")
    private String roomSrc;

    @JSONField(name = "room_name")
    private String roomName;

    private String nickname;

    private Integer hn;

    private Boolean isConnected;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomSrc() {
        return roomSrc;
    }

    public void setRoomSrc(String roomSrc) {
        this.roomSrc = roomSrc;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getHn() {
        return hn;
    }

    public void setHn(Integer hn) {
        this.hn = hn;
    }

    public Boolean isConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomVO{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", roomSrc='").append(roomSrc).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append(", nickname='").append(nickname).append('\'');
        sb.append(", hn=").append(hn);
        sb.append(", isConnected=").append(isConnected);
        sb.append('}');
        return sb.toString();
    }
}
