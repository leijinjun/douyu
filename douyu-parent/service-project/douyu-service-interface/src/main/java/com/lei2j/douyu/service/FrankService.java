package com.lei2j.douyu.service;

import com.lei2j.douyu.pojo.FrankEntity;
import com.lei2j.douyu.qo.FrankQuery;
import com.lei2j.douyu.vo.FrankVo;

import java.util.List;
/**
 * @author lei2j
 */
public interface FrankService {

    /**
     * add frank
     * @param frank
     */
    void addFrank(FrankEntity frank);

    /**
     * 获取某房间数据
     * @param frankQO
     * @return
     */
    List<FrankVo> findByCondition(FrankQuery frankQO);

    /**
     * 查询某时间段粉丝人数
     * @param frankQO
     * @return
     */
    List<Object[]> findStatisticByTimes(FrankQuery frankQO);
}
