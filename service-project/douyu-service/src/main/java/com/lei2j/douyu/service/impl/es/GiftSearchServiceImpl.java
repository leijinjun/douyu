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

package com.lei2j.douyu.service.impl.es;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.function.IndexSearchConvert;
import com.lei2j.douyu.qo.GiftQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.service.es.GiftSearchService;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.vo.GiftVo;
import com.lei2j.douyu.web.response.Pagination;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
@Service
public class GiftSearchServiceImpl extends CommonSearchService implements GiftSearchService {

    @Override
    public Pagination<GiftVo, SearchPage> query(Pagination<GiftVo, SearchPage> pagination) {
        pagination = super.search(pagination, GiftIndex.INDEX_NAME, GiftIndex.TYPE_NAME,
                (it) -> IndexSearchConvert.convertToList(it, GiftVo.class));
        return pagination;
    }

    @Override
    public BigDecimal getToDayGiftSumAggregationByRoom(Integer room) {
        LocalDateTime startTime = getTodayStart();
        LocalDateTime endTime = getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", room))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(startTime),true)
                        .to(DateUtil.localDateTimeFormat(endTime),true)
                )
                .must(QueryBuilders.rangeQuery("pc").gt(0));
        //统计礼物总计
        String key = "TODAY_GIFT_SUM";
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(AggregationBuilders.sum(key).script(new Script("doc['pc'].value*doc['gfcnt'].value")));
        logger.info("[getToDayGiftSumAggregationByRoom]ElasticSearch查询参数:{}",searchRequestBuilder.toString());
        SearchResponse userCountsResponse = searchRequestBuilder.get();
        BigDecimal sum = BigDecimal.ZERO;
        if (userCountsResponse.status() == RestStatus.OK) {
            InternalSum vaInternalSum = userCountsResponse.getAggregations().get(key);
            sum = new BigDecimal(vaInternalSum.getValue());
            logger.info("sum:{}", sum);
        }
        return sum;
    }

    @Override
    public Integer getToDayGiftUserCountsAggregationByRoom(Integer room) {
        LocalDateTime startTime = getTodayStart();
        LocalDateTime endTime = getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", room))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(startTime),true)
                        .to(DateUtil.localDateTimeFormat(endTime),true)
                )
                .must(QueryBuilders.rangeQuery("pc").from(0,false));
        String key = "TODAY_GIFT_USER_COUNTS";
        CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality(key).field("uid").precisionThreshold(100);
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("ElasticSearch查询参数:{}",searchRequestBuilder.toString());
        SearchResponse userCountsResponse = searchRequestBuilder.get();
        int giftUserCounts = 0;
        if (RestStatus.OK == userCountsResponse.status()) {
            Cardinality cardinality = userCountsResponse.getAggregations().get(key);
            Long value = cardinality.getValue();
            giftUserCounts = value.intValue();
        }
        return giftUserCounts;
    }

    @Override
    public Map<String,Object> getGiftSumIntervalDayByRoom(GiftQuery giftQO) {
        LocalDate start = giftQO.getStart();
        LocalDate end = giftQO.getEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", giftQO.getRid()))
                .must(QueryBuilders.rangeQuery("pc").from(0, false))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(LocalDateTime.of(start, LocalTime.of(0, 0, 0))), true)
                        .to(DateUtil.localDateTimeFormat(LocalDateTime.of(end, LocalTime.of(23, 59, 59))), true));
        String key = "INTERVAL_DAY_GIFT_SUM";
        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(key).field("createAt")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .minDocCount(0L)
                .format(DateFormatConstants.DATETIME_FORMAT)
                .order(BucketOrder.key(true))
                .extendedBounds(new ExtendedBounds(
                        DateUtil.localDateTimeFormat(LocalDateTime.of(start, LocalTime.of(0, 0, 0))),
                        DateUtil.localDateTimeFormat(LocalDateTime.of(end, LocalTime.of(23, 59, 59)))))
                .subAggregation(AggregationBuilders.sum("SUB_INTERVAL_DAY_GIFT_SUM").script(new Script("doc['pc'].value*doc['gfcnt'].value")));
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("ElasticSearch查询参数：{}",searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        Map<String,Object> map = new HashMap<>(7);
        if (RestStatus.OK == searchResponse.status()) {
            InternalDateHistogram dateHistogramInterval = searchResponse.getAggregations().get(key);
            List<InternalDateHistogram.Bucket> buckets = dateHistogramInterval.getBuckets();
            for (InternalDateHistogram.Bucket item:
                 buckets) {
                InternalAggregations internalAggregations = (InternalAggregations) item.getAggregations();
                InternalSum aggregation = (InternalSum) internalAggregations.asList().get(0);
                DateTime dateTime = (DateTime) item.getKey();
                map.put(dateTime.toLocalDate().toString(DateFormatConstants.DATE_FORMAT), aggregation.getValue());
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getIntervalDayPersonCountsByRoom(GiftQuery giftQO) {
        LocalDate start = giftQO.getStart();
        LocalDate end = giftQO.getEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", giftQO.getRid()))
                .must(QueryBuilders.rangeQuery("pc").from(0, false))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(LocalDateTime.of(start, LocalTime.of(0, 0, 0))), true)
                        .to(DateUtil.localDateTimeFormat(LocalDateTime.of(end, LocalTime.of(23, 59, 59))), true));
        String key = "INTERVAL_DAY_GIFT_PERSONS_COUNTS";
        String subKey = "SUB_INTERVAL_DAY_GIFT_PERSONS_COUNTS";
        AggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(key).field("createAt")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .minDocCount(0L)
                .format(DateFormatConstants.DATETIME_FORMAT)
                .order(BucketOrder.key(true))
                .extendedBounds(new ExtendedBounds(
                        DateUtil.localDateTimeFormat(LocalDateTime.of(start, LocalTime.of(0, 0, 0))),
                        DateUtil.localDateTimeFormat(LocalDateTime.of(end, LocalTime.of(23, 59, 59)))))
                .subAggregation(AggregationBuilders.cardinality(subKey).field("uid").precisionThreshold(100));
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("ElasticSearch查询参数：{}",searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        Map<String,Integer> dataMap = new HashMap<>(7);
        if (searchResponse.status() == RestStatus.OK) {
            InternalDateHistogram dateHistogramInterval = searchResponse.getAggregations().get(key);
            List<InternalDateHistogram.Bucket> buckets = dateHistogramInterval.getBuckets();
            for (InternalDateHistogram.Bucket item :
                    buckets) {
                Cardinality cardinality = item.getAggregations().get(subKey);
                Long value = cardinality.getValue();
                DateTime dateTime = (DateTime) item.getKey();
                dataMap.put(dateTime.toLocalDate().toString(DateFormatConstants.DATE_FORMAT), value.intValue());
            }
        }
        return dataMap;
    }

	@Override
	public List<Map<String, Object>> getToDayGiftTopSum(GiftQuery giftQO) {
		LocalDate start = giftQO.getStart();
        LocalDate end = giftQO.getEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", giftQO.getRid()))
                .must(QueryBuilders.rangeQuery("pc").from(0, false))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(LocalDateTime.of(start, LocalTime.of(0, 0, 0))), true)
                        .to(DateUtil.localDateTimeFormat(LocalDateTime.of(end, LocalTime.of(23, 59, 59))), true));
        String key = "TODAY_GIFT_TOP_SUM";
        String subKey = "SUB_TODAY_GIFT_TOP_SUM";
        String topHitKey = "TOP_HIT";
        List<String> fieldDataFields = new ArrayList<>(2);
        fieldDataFields.add("ic");
        fieldDataFields.add("nn");
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(key).field("uid").size(7).order(BucketOrder.aggregation(subKey,false))
        		.subAggregation(
        				AggregationBuilders.sum(subKey).script(new Script("doc['pc'].value*doc['gfcnt'].value"))
        				)
        		.subAggregation(AggregationBuilders.topHits(topHitKey).fieldDataFields(fieldDataFields).size(1));
        SearchResponse searchResponse = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
        	.setQuery(queryBuilder)
        	.addAggregation(aggregationBuilder)
        	.setSize(0)
        	.get();
        List<Map<String, Object>> list = new ArrayList<>(7);
        if(searchResponse.status()==RestStatus.OK) {
            LongTerms longTerms = searchResponse.getAggregations().get(key);
            List<Bucket> buckets = longTerms.getBuckets();
            for (Bucket item :
                    buckets) {
                Map<String, Object> dataMap = new HashMap<>(3);
                //用户uid
                Object uid = item.getKey();
                InternalAggregations internalAggregations = (InternalAggregations) item.getAggregations();
                InternalSum internalSum = internalAggregations.get(subKey);
                //礼物价值
                double giftValue = internalSum.getValue();
                InternalTopHits internalTopHits = internalAggregations.get(topHitKey);
                SearchHit searchHit = internalTopHits.getHits().getAt(0);
                //用户昵称
                Object nn = searchHit.field("nn").getValue();
                Object ic = searchHit.field("ic").getValue();
                dataMap.put("uid", uid);
                dataMap.put("value", giftValue);
                dataMap.put("nn", nn);
                dataMap.put("ic", ic);
                list.add(dataMap);
            }
        }
        return list;
	}

    @Override
    public Map<String,BigDecimal> getToDayGiftByRoomId(Integer topSize) {
        LocalDateTime start = super.getTodayStart();
        LocalDateTime end = super.getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("pc").from(0, false))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(start), true)
                        .to(DateUtil.localDateTimeFormat(end), true));
        String roomKey = "ROOM_KEY";
        String subSumRoomKey = "SUM_ROOM_KEY";
        TermsAggregationBuilder termsAggregationBuilder =
                AggregationBuilders.terms(roomKey)
                        .field("rid")
                        .size(topSize)
                        .order(BucketOrder.aggregation(subSumRoomKey,false))
                        .subAggregation(
                                AggregationBuilders.sum(subSumRoomKey)
                                        .script(new Script("doc['pc'].value*doc['gfcnt'].value"))
                        );

        SearchResponse searchResponse = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder)
                .setSize(0)
                .get();
        if(searchResponse.status()==RestStatus.OK){
            LongTerms longTerms = searchResponse.getAggregations().get(roomKey);
            List<Bucket> buckets = longTerms.getBuckets();
            Map<String,BigDecimal> dataMap = new LinkedHashMap<>(buckets.size());
            for (Bucket item:
                    buckets) {
                String rid = item.getKeyAsString();
                InternalSum internalSum = item.getAggregations().get(subSumRoomKey);
                BigDecimal value = new BigDecimal(internalSum.getValue()).setScale(2, RoundingMode.HALF_UP);
                dataMap.put(rid,value);
            }
            return dataMap;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTodayGiftMoneyByUserId(Integer topSize) {
        LocalDateTime start = super.getTodayStart();
        LocalDateTime end = super.getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("pc").from(0, false))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(start), true)
                        .to(DateUtil.localDateTimeFormat(end), true));
        String key = "MONEY_KEY";
        String subKey = "SUB_MONEY_KEY";
        String fieldKeys = "FIELD_KEYS";
        List<String> fieldDataFields = new ArrayList<>(2);
        fieldDataFields.add("ic");
        fieldDataFields.add("nn");
        AggregatorFactories.Builder builder = new AggregatorFactories.Builder();
        builder.addAggregator(AggregationBuilders.sum(subKey)
                .script(new Script("doc['pc'].value*doc['gfcnt'].value")));
        builder.addAggregator(AggregationBuilders.topHits(fieldKeys).fieldDataFields(fieldDataFields));
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms(key).size(topSize)
                .field("uid").order(BucketOrder.aggregation(subKey, false))
                .subAggregations(builder);
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(GiftIndex.INDEX_NAME).setTypes(GiftIndex.TYPE_NAME)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder)
                .setSize(0);
        logger.info("[getTodayGiftMoneyByUserId]统计参数:{}",searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        if(searchResponse.status()==RestStatus.OK){
            List<Map<String, Object>> mapList = new ArrayList<>();
            LongTerms longTerms = searchResponse.getAggregations().get(key);
            List<Bucket> buckets = longTerms.getBuckets();
            for (Bucket bucket :
                    buckets) {
                Number uid = bucket.getKeyAsNumber();
                Aggregations aggregations = bucket.getAggregations();
                InternalSum internalSum = aggregations.get(subKey);
                InternalTopHits internalTopHits = aggregations.get(fieldKeys);
                //礼物价值
                double giftValue = internalSum.getValue();
                SearchHit searchHit = internalTopHits.getHits().getAt(0);
                //用户昵称
                Object nn = searchHit.field("nn").getValue();
                Object ic = searchHit.field("ic").getValue();
                Map<String,Object> map = new HashMap<>(4);
                map.put("uid",uid);
                map.put("money",new BigDecimal(giftValue).setScale(2,RoundingMode.HALF_UP));
                map.put("nn",nn);
                map.put("ic",ic);
                mapList.add(map);
            }
            return mapList;
        }
        return null;
    }
}
