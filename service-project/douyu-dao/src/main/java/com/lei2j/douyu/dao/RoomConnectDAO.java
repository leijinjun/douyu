package com.lei2j.douyu.dao;

import com.lei2j.douyu.core.dao.CommonDAO;
import com.lei2j.douyu.dao.plugin.PageHelper;
import com.lei2j.douyu.pojo.RoomConnectEntity;
import com.lei2j.douyu.qo.RoomConnectQuery;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei2j on 2018/11/18.
 */
@Repository
public class RoomConnectDAO extends CommonDAO<RoomConnectEntity>{

    public Integer findCountByCondition(RoomConnectQuery roomConnectQuery) {
        StringBuilder sb = new StringBuilder("select count(1) from dy_room_connect where 1=1 ");
        List<Object> argList = new ArrayList<>();
        appendParams(roomConnectQuery,sb,argList);
        Object[] args = argList.toArray(new Object[0]);
        logger.info("args:{}",argList);
        String sql = sb.toString();
        return jdbcTemplate.queryForObject(sql,args,new SingleColumnRowMapper<>(Integer.class));
    }

    public List<RoomConnectEntity> findListByCondition(RoomConnectQuery roomConnectQuery) {
        StringBuilder sb = new StringBuilder("select id,room_id roomId,connect connect,owner_name ownerName from dy_room_connect where 1=1 ");
        List<Object> argList = new ArrayList<>();
        appendParams(roomConnectQuery,sb,argList);
        Object[] args = argList.toArray(new Object[0]);
        logger.info("args:{}",argList);
        String sql = sb.toString();
        return jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(RoomConnectEntity.class));
    }

    private void appendParams(RoomConnectQuery roomConnectQuery, StringBuilder sb, List<Object> argList){
        if (roomConnectQuery != null) {
            if (roomConnectQuery.getRoomId() != null) {
                argList.add(roomConnectQuery.getRoomId());
                sb.append(" and room_id = ? ");
            }
            if (roomConnectQuery.getConnect() != null) {
                argList.add(roomConnectQuery.getConnect());
                sb.append(" and connect = ? ");
            }
        }
        PageHelper.Page page = PageHelper.removeValue();
        if (page != null) {
            sb.append(" limit ?,? ");
            argList.add(page.getOffset());
            argList.add(page.getLimit());
        }
    }
}
