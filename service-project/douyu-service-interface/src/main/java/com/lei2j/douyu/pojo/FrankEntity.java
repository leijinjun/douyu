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

package com.lei2j.douyu.pojo;

import com.lei2j.douyu.core.pojo.UpdateEntity;

import javax.persistence.Table;
/**
 * @author lei2j
 */
@Table(name = "dy_frank")
public class FrankEntity extends UpdateEntity {

	private static final long serialVersionUID = 3920812192295146874L;

	private Long id;

    private Integer rid;

    private Integer fc;

    private String bnn;

    private String top10;

    public FrankEntity() {
    }

    public FrankEntity(Long id, Integer rid, Integer fc, String bnn, String top10) {
        this.id = id;
        this.rid = rid;
        this.fc = fc;
        this.bnn = bnn;
        this.top10 = top10;
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

    public String getTop10() {
        return top10;
    }

    public void setTop10(String top10) {
        this.top10 = top10;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FrankEntity{");
        sb.append("id=").append(id);
        sb.append(", rid=").append(rid);
        sb.append(", fc=").append(fc);
        sb.append(", bnn='").append(bnn).append('\'');
        sb.append(", top10='").append(top10).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
