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

/**
 * @author lei2j
 */
@Table(name = "dy_room")
public class RoomEntity extends UpdateEntity {

	private static final long serialVersionUID = -6849626665125935338L;

	@Id
    private Long id;

    private Integer rid;

    private String ownerName;

    private String roomName;

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomVo{");
        sb.append("id=").append(id);
        sb.append(", rid=").append(rid);
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", roomName='").append(roomName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
