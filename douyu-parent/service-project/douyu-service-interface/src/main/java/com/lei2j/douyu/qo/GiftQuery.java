package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class GiftQuery implements Serializable {

    private Integer rid;

    @DateTimeFormat(pattern = Constants.DATETIME_FORMAT)
    private LocalDateTime start;
    @DateTimeFormat(pattern = Constants.DATETIME_FORMAT)
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
