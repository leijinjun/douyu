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

package com.lei2j.douyu.service.impl;

import com.lei2j.douyu.dao.RoomConnectDAO;
import com.lei2j.douyu.dao.plugin.PageHelper;
import com.lei2j.douyu.pojo.RoomConnectEntity;
import com.lei2j.douyu.qo.RoomConnectQuery;
import com.lei2j.douyu.service.RoomConnectService;
import com.lei2j.douyu.web.response.Pagination;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei2j on 2018/11/18.
 */
@Component
public class RoomConnectServiceImpl extends BaseServiceImpl implements RoomConnectService{

    @Resource
    private RoomConnectDAO roomConnectDAO;

    @Override
    public Pagination<RoomConnectEntity, RoomConnectQuery> getPageByCondition(Pagination<RoomConnectEntity, RoomConnectQuery> pagination) {
        RoomConnectQuery params = pagination.getParams();
        Integer total = roomConnectDAO.findCountByCondition(params);
        Integer offset = pagination.getOffset();
        if(offset<total){
            PageHelper.startPage(offset,pagination.getLimit());
            List<RoomConnectEntity> list = roomConnectDAO.findListByCondition(params);
            pagination.setItems(list);
        }else{
            pagination.setItems(new ArrayList<>(0));
        }
        pagination.setTotal(total);
        return pagination;
    }

    @Override
    public Integer addRoomConnect(RoomConnectEntity roomConnectEntity) {
        roomConnectEntity.setCreateAt(new Timestamp(System.currentTimeMillis()));
        roomConnectDAO.insertSelective(roomConnectEntity);
        return roomConnectEntity.getId();
    }

    @Override
    public void updateRoomConnect(RoomConnectEntity roomConnectEntity) {
        roomConnectEntity.setUpdateAt(new Timestamp(System.currentTimeMillis()));
        roomConnectDAO.updateSelective(roomConnectEntity);
    }
}
