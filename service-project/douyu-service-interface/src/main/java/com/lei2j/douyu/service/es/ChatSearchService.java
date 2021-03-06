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

package com.lei2j.douyu.service.es;

import com.lei2j.douyu.qo.ChatQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.vo.DanmuSearchView;
import com.lei2j.douyu.web.response.Pagination;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
public interface ChatSearchService {

    /**
     * 根据条件查询弹幕数据
     * @param pagination pagination
     * @return Pagination
     */
    Pagination<ChatMessageVo,SearchPage> query(Pagination<ChatMessageVo, SearchPage> pagination);

    /**
     * 统计今日房间弹幕人数
     * @param room room
     * @return Integer
     */
    Integer getToDayUserCountsAggregationByRoom(Integer room);

    /**
     * 根据时间段统计每天弹幕数
     * @param chatQO chatQO
     * @return Map
     */
    Map<String,Integer> getIntervalDayChatSumByRoom(ChatQuery chatQO);

    /**
     * 根据时间段统计每天弹幕人数
     * @param chatQO chatQO
     * @return Map
     */
    Map<String,Integer> getIntervalDayChatPersonCountsByRoom(ChatQuery chatQO);

    /**
     * 当天主播弹幕排行榜
     * @return Map
     */
    Map<String, Long> getTodayDanmuSumByRoomId(Integer topSize);

    Pagination<DanmuSearchView, SearchPage> queryDanmuByCondition(Pagination<DanmuSearchView, SearchPage> pagination);

}
