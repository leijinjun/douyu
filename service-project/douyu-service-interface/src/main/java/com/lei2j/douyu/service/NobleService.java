package com.lei2j.douyu.service;

import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.qo.NobleQuery;

import java.util.List;

public interface NobleService {

    void addNoble(NobleEntity noble);

    List<NobleEntity> findByCondition(NobleQuery nobleQO);
}
