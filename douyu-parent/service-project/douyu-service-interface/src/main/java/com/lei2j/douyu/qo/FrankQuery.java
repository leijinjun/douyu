package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.pojo.FrankEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class FrankQuery extends FrankEntity {

	private static final long serialVersionUID = -1185213236452170315L;
	
	@DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate start;
    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
    private LocalDate end;

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
