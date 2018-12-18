package com.lei2j.douyu.qo;

import com.lei2j.douyu.core.constant.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class ChatQuery implements Serializable {

    private String nn;
    
    private Long uid;

    private Integer rid;

    @DateTimeFormat(pattern = Constants.DATETIME_FORMAT)
	@NotNull
    private LocalDateTime start;
	@DateTimeFormat(pattern = Constants.DATETIME_FORMAT)
	@NotNull
    private LocalDateTime end;
    
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
