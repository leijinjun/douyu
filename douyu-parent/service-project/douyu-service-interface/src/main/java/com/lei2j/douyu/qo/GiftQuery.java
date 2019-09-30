package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author lei2j
 */
public class GiftQuery {

    private Integer rid;

    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate start;
    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate end;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}