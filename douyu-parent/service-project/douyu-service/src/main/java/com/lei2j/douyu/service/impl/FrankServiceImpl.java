package com.lei2j.douyu.service.impl;

import com.lei2j.douyu.dao.FrankDAO;
import com.lei2j.douyu.pojo.FrankEntity;
import com.lei2j.douyu.qo.FrankQuery;
import com.lei2j.douyu.service.FrankService;
import com.lei2j.douyu.vo.FrankVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lei2j
 */
@Service
public class FrankServiceImpl extends BaseServiceImpl implements FrankService {

    @Resource
    private FrankDAO frankDAO;

    @Override
    public void addFrank(FrankEntity frank){
        frankDAO.insertSelective(frank);
    }

    @Override
    public List<FrankVo> findByCondition(FrankQuery frankQO) {
        List<FrankVo> frankViewQOS = frankDAO.findFrankByCondition(frankQO);
        return frankViewQOS;
    }

    @Override
    public List<Object[]> findStatisticByTimes(FrankQuery frankQO) {
        return frankDAO.findStatisticByTimes(frankQO);
    }
}
