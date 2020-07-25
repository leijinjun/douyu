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

public class CateVo implements Serializable {

	private static final long serialVersionUID = -7173167775501577731L;

	private String cateName;

    private String alias;

    private String cateId;

    private String img;

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CateVo{");
        sb.append("cateName='").append(cateName).append('\'');
        sb.append(", alias='").append(alias).append('\'');
        sb.append(", cateId='").append(cateId).append('\'');
        sb.append(", img='").append(img).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
