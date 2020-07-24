package com.lei2j.douyu.pojo;

import com.lei2j.douyu.core.pojo.UpdateEntity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lei2j on 2018/11/18.
 */
@Table(name = "dy_room_connect")
public class RoomConnectEntity extends UpdateEntity{

    private static final long serialVersionUID = 8320417496367049786L;

    @Id
    private Integer id;

    private Integer roomId;

    private String ownerName;

    private Integer connect;

    public RoomConnectEntity() {
    }

    public RoomConnectEntity(Integer id, Integer roomId, String ownerName, Integer connect) {
        this.id = id;
        this.roomId = roomId;
        this.ownerName = ownerName;
        this.connect = connect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getConnect() {
        return connect;
    }

    public void setConnect(Integer connect) {
        this.connect = connect;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomConnectEntity{");
        sb.append("createAt=").append(createAt);
        sb.append(", id=").append(id);
        sb.append(", updateAt=").append(updateAt);
        sb.append(", roomId=").append(roomId);
        sb.append(", ownerName='").append(ownerName).append('\'');
        sb.append(", connect=").append(connect);
        sb.append('}');
        return sb.toString();
    }
}
