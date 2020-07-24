package com.lei2j.douyu.service.impl;

import com.lei2j.douyu.dao.NobleDAO;
import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.qo.NobleQuery;
import com.lei2j.douyu.service.NobleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lei2j
 */
@Service
public class NobleServiceImpl extends BaseServiceImpl implements NobleService {

    @Resource
    private NobleDAO nobleDAO;

    @Override
    public void addNoble(NobleEntity noble) {
        nobleDAO.insertSelective(noble);
    }

    @Override
    public List<NobleEntity> findByCondition(NobleQuery nobleQO) {
        return nobleDAO.findList(nobleQO);
    }
}
