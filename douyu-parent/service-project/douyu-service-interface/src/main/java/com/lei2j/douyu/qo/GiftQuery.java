package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class GiftQuery {

    private Integer rid;

    @DateTimeFormat(pattern = DateFormatConstants.DATETIME_FORMAT)
    private LocalDateTime start;
    @DateTimeFormat(pattern = DateFormatConstants.DATETIME_FORMAT)
    private LocalDateTime end;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

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
