package com.lei2j.douyu.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public class RoomDetailVo implements Serializable{

	private static final long serialVersionUID = 1L;

    /**
     * 房间
     */
	private Integer roomId;

    /**
     * 房间图片
     */
    private String roomThumb;

    /**
     * 分类id
     */
    private String cateId;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * //房间开播状态，1=正在直播，2=还未开播
     */
    private Integer roomStatus;

    /**
     * //房间拥有者昵称
     */
    private String ownerName;

    /**
     * //房间人气
     */
    private Integer hn;

    /**
     * //粉丝数
     */
    private Integer fansNum;

    private List<RoomGiftVo> roomGifts;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomThumb() {
        return roomThumb;
    }

    public void setRoomThumb(String roomThumb) {
        this.roomThumb = roomThumb;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getHn() {
		return hn;
	}

	public void setHn(Integer hn) {
		this.hn = hn;
	}

	public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public List<RoomGiftVo> getRoomGifts() {
        return roomGifts;
    }

    public void setRoomGifts(List<RoomGiftVo> roomGifts) {
        this.roomGifts = roomGifts;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomDetailVO{");
        sb.append("roomId='").append(roomId).append('\'');
        sb.append(", roomThumb='").append(roomThumb).append('\'');
        sb.append(", cateId='").append(cateId).append('\'');
        sb.append(", cateName='").append(cateName).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append(", roomStatus=").append(roomStatus);
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", hn=").append(hn);
        sb.append(", fansNum=").append(fansNum);
        sb.append(", roomGifts=").append(roomGifts);
        sb.append('}');
        return sb.toString();
    }
}
