package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author lei2j
 */
public class ChatQuery {

    private String nn;
    
    private Long uid;

    private Integer rid;

    @DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
	@NotNull
    private LocalDate start;
	@DateTimeFormat(pattern = DateFormatConstants.DATE_FORMAT)
	@NotNull
    private LocalDate end;
    
    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

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
