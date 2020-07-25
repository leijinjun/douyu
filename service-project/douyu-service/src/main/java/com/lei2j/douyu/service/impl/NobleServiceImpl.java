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
