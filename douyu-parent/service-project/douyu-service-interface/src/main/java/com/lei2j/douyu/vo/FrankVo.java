package com.lei2j.douyu.vo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author lei2j
 */
public class FrankVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer rid;

    private Integer fc;

    private String bnn;

    private LocalDate currentDate;

    public FrankVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }

    public String getBnn() {
        return bnn;
    }

    public void setBnn(String bnn) {
        this.bnn = bnn;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}

