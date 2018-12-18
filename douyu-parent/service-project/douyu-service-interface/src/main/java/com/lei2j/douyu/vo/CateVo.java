package com.lei2j.douyu.vo;

import java.io.Serializable;

public class CateVo implements Serializable {

	private static final long serialVersionUID = -7173167775501577731L;

	private String cateName;

    private String alias;

    private Integer cateId;

    private String img;

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
