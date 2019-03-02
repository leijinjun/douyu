package com.lei2j.douyu.service.es;

import com.lei2j.douyu.qo.GiftQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.vo.GiftVo;
import com.lei2j.douyu.web.response.Pagination;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
public interface GiftSearchService {

    /**
     * 根据条件查询礼物数据
     * @param pagination
     * @return
     */
    Pagination<GiftVo,SearchPage> query(Pagination<GiftVo, SearchPage> pagination);

    /**
     * 房间今日礼物统计
     * @param room
     * @return
     */
    BigDecimal getToDayGiftSumAggregationByRoom(Integer room);

    /**
     * 房间今日送礼人数统计
     * @param room
     * @return
     */
    Integer getToDayGiftUserCountsAggregationByRoom(Integer room);

    /**
     * 根据时间段统计每天礼物总计
     * @param giftQO
     * @return
     */
    Map<String,Object> getGiftSumIntervalDayByRoom(GiftQuery giftQO);

    /**
     * 根据时间段统计每天送礼人数
     * @param giftQO
     * @return
     */
    Map<String,Integer> getIntervalDayPersonCountsByRoom(GiftQuery giftQO);

	/**
	 * 获取今日礼物土豪榜
	 * @param giftQO
	 * @return
	 */
	List<Map<String, Object>> getToDayGiftTopSum(GiftQuery giftQO);
}
