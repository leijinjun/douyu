package com.lei2j.douyu.vo;

import java.time.LocalDateTime;

/**
 * author: lei2j
 * date: 2019/4/1
 */

public class DanmuSearchWithUserView {

    private String roomId;

    private String ownerName;

    private String nn;

    private String ic;

    private String txt;

    private String createAt;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
