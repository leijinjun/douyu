package com.lei2j.douyu.dao;

import com.lei2j.douyu.core.dao.CommonDAO;
import com.lei2j.douyu.dao.plugin.PageHelper;
import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.qo.NobleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lei2j
 */
@Repository
public class NobleDAO extends CommonDAO<NobleEntity>{

    public List<NobleEntity> findList(NobleQuery nobleQO){
        StringBuilder sb = new StringBuilder();
        sb.append("select id id,num num,rid rid,nl nl ,create_at createAt ")
          .append(" from dy_noble ")
          .append(" where 1=1 ");
        List<Object> argList = new ArrayList<>();
        appendParams(nobleQO,sb,argList);
        Object[] args = argList.toArray(new Object[0]);
        logger.info("args:{}",argList);
        String sql = sb.toString();
        List<NobleEntity> nobleList = jdbcTemplate.query(sql,args,new BeanPropertyRowMapper<>(NobleEntity.class));
        return nobleList;
    }

    private void appendParams(NobleQuery nobleQO, StringBuilder sb, List<Object> argList){
		if (nobleQO != null) {
			if (nobleQO.getRid() != null) {
				argList.add(nobleQO.getRid());
				sb.append(" and rid=? ");
			}
			if (nobleQO.getStart() != null) {
				argList.add(nobleQO.getStart());
				sb.append(" and create_at >= ? ");
			}
			if (nobleQO.getEnd() != null) {
				argList.add(nobleQO.getEnd());
				sb.append(" and create_at <= ? ");
			}
			String sort = nobleQO.getSort();
			if (StringUtils.isNotBlank(sort)) {
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