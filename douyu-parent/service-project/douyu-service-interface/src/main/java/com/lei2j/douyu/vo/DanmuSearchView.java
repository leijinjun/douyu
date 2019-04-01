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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DanmuSearchView{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", nn='").append(nn).append('\'');
        sb.append(", level=").append(level);
        sb.append(", fansLevel=").append(fansLevel);
        sb.append(", ic='").append(ic).append('\'');
        sb.append(", txt='").append(txt).append('\'');
        sb.append(", createAt='").append(createAt).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
