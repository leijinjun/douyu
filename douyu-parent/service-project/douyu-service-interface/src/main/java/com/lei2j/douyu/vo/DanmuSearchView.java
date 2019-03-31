package com.lei2j.douyu.vo;

/**
 * author: lei2j
 * date: 2019/3/31
 */

public class DanmuSearchView {

    private String roomId;

    private String ownerName;

    private String nn;

    private Integer level;

    private Integer fansLevel;


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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFansLevel() {
        return fansLevel;
    }

    public void setFansLevel(Integer fansLevel) {
        this.fansLevel = fansLevel;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DanmuSearchView{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", nn='").append(nn).append('\'');
        sb.append(", level=").append(level);
        sb.append(", fansLevel=").append(fansLevel);
        sb.append('}');
        return sb.toString();
    }
}
