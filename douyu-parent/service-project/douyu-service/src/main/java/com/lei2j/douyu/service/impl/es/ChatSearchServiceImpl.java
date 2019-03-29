package com.lei2j.douyu.service.impl.es;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.qo.ChatQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.service.es.ChatSearchService;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.web.response.Pagination;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/23.
 */
@Component
public class ChatSearchServiceImpl extends CommonSearchService implements ChatSearchService {

    @Override
    public Pagination<ChatMessageVo,SearchPage> query(Pagination<ChatMessageVo,SearchPage> pagination){
        pagination = super.search(pagination,ChatMessageVo.class, ChatMessageIndex.INDEX_NAME, ChatMessageIndex.TYPE_NAME);
        return pagination;
    }

    @Override
    public Integer getToDayUserCountsAggregationnByRoom(Integer room) {
        LocalDateTime startTime = getTodayStart();
        LocalDateTime endTime = getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", room))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(startTime),true)
                        .to(DateUtil.localDateTimeFormat(endTime),true)
                );
        //统计弹幕人数
        String key = "TODAY_USER_COUNTS";
        //去重统计，precision_threshold =100 ，表示统计精度 越高统计约准确内存占用越高
        CardinalityAggregationBuilder aggregationBuilder = AggregationBuilders.cardinality(key).field("uid").precisionThreshold(100);
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(ChatMessageIndex.INDEX_NAME).setTypes(ChatMessageIndex.TYPE_NAME)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("查询参数:{}", searchRequestBuilder.toString());
        SearchResponse userCountsResponse = searchRequestBuilder.get();
        Integer userCounts = 0;
        if (userCountsResponse.status() == RestStatus.OK) {
            Cardinality cardinality = userCountsResponse.getAggregations().get(key);
            Long value = cardinality.getValue();
            logger.info("total:{}", value);
            userCounts = value.intValue();
        }
        return userCounts;
    }

    @Override
    public Map<String, Integer> getIntervalDayChatSumByRoom(ChatQuery chatQO) {
        LocalDateTime startTime = chatQO.getStart();
        LocalDateTime endTime = chatQO.getEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", chatQO.getRid()))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(startTime),true)
                        .to(DateUtil.localDateTimeFormat(endTime),true)
                );
        String key = "INTERVAL_DAY_CHAT_SUM";
        DateHistogramAggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(key).field("createAt")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .minDocCount(0L)
                .format(DateFormatConstants.DATE_FORMAT)
                .extendedBounds(new ExtendedBounds(DateUtil.localDateTimeFormat(startTime, DateFormatConstants.DATE_FORMAT), DateUtil.localDateTimeFormat(endTime, DateFormatConstants.DATE_FORMAT)));
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(ChatMessageIndex.INDEX_NAME).setTypes(ChatMessageIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("ElasticSearch查询参数：{}",searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        Map<String,Integer> map = new HashMap<>(7);
        if (RestStatus.OK == searchResponse.status()) {
            InternalDateHistogram dateHistogramInterval = searchResponse.getAggregations().get(key);
            List<InternalDateHistogram.Bucket> buckets = dateHistogramInterval.getBuckets();
            for (InternalDateHistogram.Bucket item:
                 buckets) {
                Long docCount = item.getDocCount();
                DateTime dateTime = (DateTime) item.getKey();
                map.put(dateTime.toLocalDate().toString(DateFormatConstants.DATE_FORMAT), docCount.intValue());
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getIntervalDayChatPersonCountsByRoom(ChatQuery chatQO) {
        LocalDateTime startTime = chatQO.getStart();
        LocalDateTime endTime = chatQO.getEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("rid", chatQO.getRid()))
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(startTime),true)
                        .to(DateUtil.localDateTimeFormat(endTime),true)
                );
        String key = "INTERVAL_DAY_CHAT_PERSON_COUNTS";
        String subKey = "SUB_INTERVAL_DAY_CHAT_PERSON_COUNTS";
        DateHistogramAggregationBuilder aggregationBuilder = AggregationBuilders.dateHistogram(key).field("createAt")
                .dateHistogramInterval(DateHistogramInterval.DAY)
                .minDocCount(0L)
                .format(DateFormatConstants.DATE_FORMAT)
                .extendedBounds(new ExtendedBounds(DateUtil.localDateTimeFormat(startTime, DateFormatConstants.DATE_FORMAT), DateUtil.localDateTimeFormat(endTime, DateFormatConstants.DATE_FORMAT)))
                .subAggregation(AggregationBuilders.cardinality(subKey).field("uid").precisionThreshold(100))
                ;
        SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(ChatMessageIndex.INDEX_NAME).setTypes(ChatMessageIndex.TYPE_NAME)
                .setSize(0)
                .setQuery(queryBuilder)
                .addAggregation(aggregationBuilder);
        logger.info("ElasticSearch查询参数：{}",searchRequestBuilder);
        SearchResponse searchResponse = searchRequestBuilder.get();
        Map<String,Integer> map = new HashMap<>(7);
        if(RestStatus.OK==searchResponse.status()){
            InternalDateHistogram dateHistogramInterval = searchResponse.getAggregations().get(key);
            List<InternalDateHistogram.Bucket> buckets = dateHistogramInterval.getBuckets();
            for (InternalDateHistogram.Bucket item:
                    buckets) {
                Cardinality cardinality = item.getAggregations().get(subKey);
                Long value = cardinality.getValue();
                DateTime dateTime = (DateTime)item.getKey();
                map.put(dateTime.toLocalDate().toString(DateFormatConstants.DATE_FORMAT),value.intValue());
            }
        }
        return map;
    }

    @Override
    public Map<String, Long> getTodayDanmuSumAggregation() {
        LocalDateTime todayStart = getTodayStart();
        LocalDateTime todayEnd = getTodayEnd();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("createAt")
                        .from(DateUtil.localDateTimeFormat(todayStart),true)
                        .to(DateUtil.localDateTimeFormat(todayEnd),true)
                );
        String roomKey = "ROOM_KEY";
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms(roomKey)
                .field("rid")
                .size(10)
                .order(BucketOrder.count(false));
        SearchResponse searchResponse = searchClient.client().prepareSearch(ChatMessageIndex.INDEX_NAME)
                .setTypes(ChatMessageIndex.TYPE_NAME)
                .setQuery(queryBuilder)
                .addAggregation(termsAggregationBuilder)
                .setSize(0)
                .get();
        if(searchResponse.status()==RestStatus.OK){
            LongTerms longTerms = searchResponse.getAggregations().get(roomKey);
            List<LongTerms.Bucket> buckets = longTerms.getBuckets();
            Map<String,Long> dataMap = new LinkedHashMap<>(buckets.size());
            for (LongTerms.Bucket item:
                 buckets) {
                String rid = item.getKeyAsString();
                long docCount = item.getDocCount();
                dataMap.put(rid,docCount);
            }
            return dataMap;
        }
        return null;
    }
}
