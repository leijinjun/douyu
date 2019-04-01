package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * author: lei2j
 * date: 2019/3/31
 */

public class DanmuQuery implements Serializable {

    private Integer roomId;

    private String ownerName;

    private String nn;

    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate startDate;

    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate endDate;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DanmuQuery{");
        sb.append("roomId=").append(roomId);
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", nn='").append(nn).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append('}');
        return sb.toString();
    }
}
