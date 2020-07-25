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

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "dy_noble")
public class NobleEntity extends UpdateEntity {

	private static final long serialVersionUID = -3620139861681951207L;

	@Id
	private Long id;

    private Integer rid;

    private Integer num;

    private String nl;

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

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("NobleEntity{");
        sb.append("id=").append(id);
        sb.append(", rid=").append(rid);
        sb.append(", num=").append(num);
        sb.append(", nl='").append(nl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
