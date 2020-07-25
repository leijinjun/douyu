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
        return frankDAO.findFrankByCondition(frankQO);
    }

    @Override
    public List<Object[]> findStatisticByTimes(FrankQuery frankQO) {
        return frankDAO.findStatisticByTimes(frankQO);
    }
}
