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
public class ChatMessageVo implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 弹幕唯一标识
     */
    private String cid;

    /**
     * 房间Id
     */
    private Integer rid;

    /**
     * 用户Id
     */
    private Long uid;

    /**
     * 发送者昵称
     */
    private String nn;

    /**
     * 发送内容
     */
    private String txt;

    /**
     * 发送者等级
     */
    private Integer level;

    /**
     * 弹幕颜色
     */
    private Integer col = 0;

    /**
     * 客户端标识
     */
    private Integer ct = 0;

    /**
     * 房间组权限
     */
    private Integer rg = 1;

    /**
     * 平台组权限
     */
    private Integer pg = 1;

    /**
     * 用户头像
     */
    private String ic;

    /**
     * 贵族等级
     */
    private Integer nl;

    /**
     * 贵族弹幕标识，0-非贵族弹幕，1-贵族弹幕，默认值0
     */
    private Integer nc = 0;

    /**
     * 徽章昵称
     */
    private String bnn;

    /**
     * 徽章等级
     */
    private Integer bl;

    /**
     * 徽章房间
     */
    private Integer brid;

    /**
     * 是否粉丝弹幕标记: 0-非粉丝弹幕，1-粉丝弹幕, 默认值 0
     */
    private Integer ifs = 0;

    private LocalDateTime createAt;

    public ChatMessageVo() {
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getCt() {
        return ct;
    }

    public void setCt(Integer ct) {
        this.ct = ct;
    }

    public Integer getRg() {
        return rg;
    }

    public void setRg(Integer rg) {
        this.rg = rg;
    }

    public Integer getPg() {
        return pg;
    }

    public void setPg(Integer pg) {
        this.pg = pg;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Integer getNl() {
        return nl;
    }

    public void setNl(Integer nl) {
        this.nl = nl;
    }

    public Integer getNc() {
        return nc;
    }

    public void setNc(Integer nc) {
        this.nc = nc;
    }

    public String getBnn() {
        return bnn;
    }

    public void setBnn(String bnn) {
        this.bnn = bnn;
    }

    public Integer getBl() {
        return bl;
    }

    public void setBl(Integer bl) {
        this.bl = bl;
    }

    public Integer getBrid() {
        return brid;
    }

    public void setBrid(Integer brid) {
        this.brid = brid;
    }

    public Integer getIfs() {
        return ifs;
    }

    public void setIfs(Integer ifs) {
        this.ifs = ifs;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
