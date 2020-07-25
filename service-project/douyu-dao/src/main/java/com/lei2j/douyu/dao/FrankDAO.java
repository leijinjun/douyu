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

package com.lei2j.douyu.dao;

import com.lei2j.douyu.core.dao.CommonDAO;
import com.lei2j.douyu.dao.plugin.PageHelper;
import com.lei2j.douyu.pojo.FrankEntity;
import com.lei2j.douyu.qo.FrankQuery;
import com.lei2j.douyu.vo.FrankVo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @author lei2j
 */
@Repository
public class FrankDAO extends CommonDAO<FrankEntity> {

    public List<FrankVo> findFrankByCondition(FrankQuery frankQO){
        StringBuilder sb = new StringBuilder();
        sb.append("select id id,rid rid,fc fc,bnn bnn,create_at currentDate ")
                .append(" from dy_frank ")
                .append(" where 1=1 ");
        List<Object> argList = new ArrayList<>();
        appendParams(frankQO,sb,argList);
        Object[] args = argList.toArray(new Object[0]);
        logger.info("args:{}",argList);
        String sql = sb.toString();
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(FrankVo.class),args);
    }

    public List<Object[]> findStatisticByTimes(FrankQuery frankQO){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tmp.create_date 'day',ROUND(SUM(tmp.fc)/COUNT(tmp.create_date)) 'sum' ")
                .append(" FROM ( ")
                .append(" SELECT DATE_FORMAT(create_at,'%Y-%m-%d') AS 'create_date',fc ")
                .append(" FROM dy_frank ")
                .append(" WHERE rid = ? AND create_at BETWEEN ? AND ? ")
                .append(" ) AS tmp ")
                .append(" GROUP BY tmp.create_date");
        Object[] args = new Object[]{frankQO.getRid(),frankQO.getStart(),frankQO.getEnd()};
        logger.info("args:{},{},{}",args[0],args[1],args[2]);
        String sql = sb.toString();
        List<Object[]> list = jdbcTemplate.query(sql, args,(resultSet,var2)->{
            Object[] obj = new Object[2];
            obj[0] = resultSet.getString("day");
            obj[1] = resultSet.getString("sum");
            return obj;
        });
        return list;
    }

    private void appendParams(FrankQuery frankQO,StringBuilder sb,List<Object> argList){
		if (frankQO != null) {
			if (frankQO.getRid() != null) {
				argList.add(frankQO.getRid());
				sb.append(" and rid=? ");
			}
			if (frankQO.getStart() != null) {
				argList.add(frankQO.getStart());
				sb.append(" and create_at >= ? ");
			}
			if (frankQO.getEnd() != null) {
				argList.add(frankQO.getEnd());
				sb.append(" and create_at <= ? ");
			}
			String sort = frankQO.getSort();
			if (!StringUtils.isEmpty(sort)) {
				sb.append(" ORDER BY ").append(sort);
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
