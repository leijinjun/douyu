package com.lei2j.douyu.qo;

import com.lei2j.douyu.pojo.RoomEntity;

import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class RoomQuery extends RoomEntity {

	private static final long serialVersionUID = -3620249512007177570L;

	private LocalDateTime start;

    private LocalDateTime end;

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
