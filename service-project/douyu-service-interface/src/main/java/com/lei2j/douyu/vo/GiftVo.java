/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/23.
 */
public class GiftVo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 房间ID
     */
    private Integer rid;

    /**
     * 礼物ID
     * gfid=1742,2000鱼翅
     * gfid=1735,500鱼翅
     * gfid=1696,100鱼翅
     * gfid=1695,6鱼翅
     * gfid=1698,1鱼翅
     * gfid=1693,0.1鱼翅
     */
    private Integer gfid;

    /**
     * 礼物个数
     */
    private Integer gfcnt;

    private String giftName;

    /**
     * 礼物价值
     */
    private Double pc = 0.0;

    /**
     * 礼物显示样式
     */
    private Integer gs;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户昵称
     */
    private String  nn;

    /**
     * 用户头像
     */
    private String ic;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 礼物连击数,默认1连击
     */
    private Integer hits = 1;

    /**
     *大礼物标识,默认bg=0(小礼物)
     */
    private Integer bg = 0;

    private LocalDateTime createAt;

    public GiftVo() {
    }

    public GiftVo(String id, Integer rid, Integer gfid, Integer gfcnt, String giftName, Double pc, Integer gs, Long uid, String nn, String ic, Integer level, Integer hits, Integer bg, LocalDateTime createAt) {
        this.id = id;
        this.rid = rid;
        this.gfid = gfid;
        this.gfcnt = gfcnt;
        this.giftName = giftName;
        this.pc = pc;
        this.gs = gs;
        this.uid = uid;
        this.nn = nn;
        this.ic = ic;
        this.level = level;
        this.hits = hits;
        this.bg = bg;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getGfid() {
        return gfid;
    }

    public void setGfid(Integer gfid) {
        this.gfid = gfid;
    }

    public Double getPc() {
        return pc;
    }

    public void setPc(Double pc) {
        this.pc = pc;
    }

    public Integer getGs() {
        return gs;
    }

    public void setGs(Integer gs) {
        this.gs = gs;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNn() {
        return nn;
    }

    public void setNn(String nn) {
        this.nn = nn;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getBg() {
        return bg;
    }

    public void setBg(Integer bg) {
        this.bg = bg;
    }

    public Integer getGfcnt() {
        return gfcnt;
    }

    public void setGfcnt(Integer gfcnt) {
        this.gfcnt = gfcnt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }
}
