package com.lei2j.douyu.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lei2j on 2018/8/13.
 */
public class RoomGiftVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;//礼物id

    private String name;//礼物名称

    private Integer type;//礼物类型2=鱼翅礼物,1=普通礼物

    private BigDecimal pc;//礼物价值

    private String mimg;//礼物图片

    private String himg;//礼物特效

    public RoomGiftVo() {
    }

    public RoomGiftVo(Integer id, String name, Integer type, BigDecimal pc) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pc = pc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPc() {
        return pc;
    }

    public void setPc(BigDecimal pc) {
        this.pc = pc;
    }

    public String getMimg() {
        return mimg;
    }

    public void setMimg(String mimg) {
        this.mimg = mimg;
    }

    public String getHimg() {
        return himg;
    }

    public void setHimg(String himg) {
        this.himg = himg;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomGiftVO{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", pc=").append(pc);
        sb.append(", mimg='").append(mimg).append('\'');
        sb.append(", himg='").append(himg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
