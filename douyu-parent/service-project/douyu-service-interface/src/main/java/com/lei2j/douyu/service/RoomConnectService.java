package com.lei2j.douyu.service;

import com.lei2j.douyu.pojo.RoomConnectEntity;
import com.lei2j.douyu.qo.RoomConnectQuery;
import com.lei2j.douyu.web.response.Pagination;

/**
 * Created by lei2j on 2018/11/18.
 */
public interface RoomConnectService {

    Pagination<RoomConnectEntity,RoomConnectQuery> getPageByCondition(Pagination<RoomConnectEntity, RoomConnectQuery> pagination);

    Integer addRoomConnect(RoomConnectEntity roomConnectEntity);

    void updateRoomConnect(RoomConnectEntity roomConnectEntity);
}
