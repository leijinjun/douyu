package com.lei2j.douyu.es.search;

import com.lei2j.douyu.qo.ChatQuery;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.web.response.Pagination;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
public interface ChatSearchService {

    /**
     * 根据条件查询弹幕数据
     * @param pagination
     * @return
     */
    Pagination<ChatMessageVo,SearchPage> query(Pagination<ChatMessageVo, SearchPage> pagination);

    /**
     * 统计今日房间弹幕人数
     * @param room
     * @return
     */
    Integer getToDayUserCountsAggregationnByRoom(Integer room);

    /**
     * 根据时间段统计每天弹幕数
     * @param chatQO
     * @return
     */
    Map<String,Integer> getIntervalDayChatSumByRoom(ChatQuery chatQO);

    /**
     * 根据时间段统计每天弹幕人数
     * @param chatQO
     * @return
     */
    Map<String,Integer> getIntervalDayChatPersonCountsByRoom(ChatQuery chatQO);
}
