package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.pojo.FrankEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class FrankQuery extends FrankEntity {

	private static final long serialVersionUID = -1185213236452170315L;
	
	@DateTimeFormat(pattern = DateFormatConstants.DATETIME_FORMAT)
    private LocalDateTime start;
    @DateTimeFormat(pattern = DateFormatConstants.DATETIME_FORMAT)
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
