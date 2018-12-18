package com.lei2j.douyu.qo;

import com.lei2j.douyu.pojo.NobleEntity;

import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class NobleQuery extends NobleEntity {

	private static final long serialVersionUID = 4724371757563254850L;

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
