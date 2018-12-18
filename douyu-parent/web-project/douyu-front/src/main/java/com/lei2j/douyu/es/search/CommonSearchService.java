package com.lei2j.douyu.es.search;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import com.lei2j.douyu.web.response.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lei2j
 */
public class CommonSearchService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
    protected ElasticSearchClient searchClient;
	
	protected <T,P extends SearchPage> Pagination<T,P> search(Pagination<T,P> pagination, Class<T> mapperClass, String indexName, String typeName) {
        SearchPage searchPage = pagination.getParams();
        Integer pageNum = pagination.getPageNum();
        Integer limit = pagination.getLimit();
        pagination.setPageNum(pageNum);
        pagination.setLimit(limit);
        SearchResponse response;
        if(searchPage.isScroll()){
            response = deepPage(searchPage,indexName,typeName);
        }else{
            response = shallowPage(pagination,indexName,typeName);
        }
        if(RestStatus.OK!=response.status()){
            throw new RuntimeException("查询数据错误");
        }
        SearchHits responseHits = response.getHits();
        Iterator<SearchHit> it = responseHits.iterator();
        pagination.setTotal(((Long)responseHits.getTotalHits()).intValue());
        List<T> list = convert(it,mapperClass);
        if(searchPage.isScroll()){
            int totalCount = pagination.getTotal();
            logger.info("查询总数:{}",totalCount);
            List<T> totalList = new ArrayList<>(totalCount);
            totalList.addAll(list);
            while (list.size()!=0){
                response = deepPage(searchPage,indexName,typeName);
                list = convert(response.getHits().iterator(), mapperClass);
                totalList.addAll(list);
            }
            list = totalList;
            String scrollId = searchPage.getScrollId();
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            boolean succeeded = searchClient.client().clearScroll(clearScrollRequest).actionGet().isSucceeded();
            logger.info("clear scrollId:{},status:{}",scrollId,succeeded);
        }
        pagination.setItems(list);
        return  pagination;
	}

    /**
     * 普通分页(from-size)
     * @param pagination
     * @param indexName
     * @param typeName
     * @return
     */
	private <T,P extends SearchPage> SearchResponse shallowPage(Pagination<T,P> pagination,String indexName,String typeName){
        SearchPage searchPage = pagination.getParams();
        SearchRequestBuilder searchBuilder = searchClient.client().prepareSearch(indexName)
                .setTypes(typeName)
                //查询器
                .setQuery(searchPage.getQueryBuilder())
                //过滤器
                .setPostFilter(searchPage.getFilterBuilder());
        //分页
        searchBuilder = searchBuilder.setFrom(pagination.getOffset()).setSize(pagination.getLimit());
        searchBuilder.setExplain(true);
        String sort = searchPage.getSort();
        searchBuilder = sortBuilder(searchBuilder, sort);
        logger.info("查询参数:{}",searchBuilder.toString());
        SearchResponse response = searchBuilder.get();
        return response;
    }

    /**
     * 深分页，使用scroll进行分页
     * @param searchPage
     * @param indexName
     * @param typeName
     * @return
     */
	private SearchResponse deepPage(SearchPage searchPage,String indexName,String typeName){
        String scrollId = searchPage.getScrollId();
        SearchResponse response;
        if(scrollId==null){
            SearchRequestBuilder searchRequestBuilder = searchClient.client().prepareSearch(indexName)
                    .setTypes(typeName)
                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                    //查询器
                    .setQuery(searchPage.getQueryBuilder())
                    //过滤器
                    .setPostFilter(searchPage.getFilterBuilder())
                    .setSize(1000)
                    //设置使用scroll
                    .setScroll(TimeValue.timeValueMinutes(1));
            logger.info("查询参数:{}",searchRequestBuilder.toString());
            response = searchRequestBuilder.get();
            scrollId = response.getScrollId();
            searchPage.setScrollId(scrollId);
            logger.info("获取到scrollId:{}",scrollId);
        }else{
            response = searchClient.client().prepareSearchScroll(scrollId).setScroll(TimeValue.timeValueMinutes(1)).get();
        }
        return  response;
    }

    private <T> List<T> convert(Iterator<SearchHit> it,Class<T> mapperClass){
        List<T> list = new ArrayList<>(10);
        if(it!=null){
            while (it.hasNext()){
                SearchHit next = it.next();
                Map<String, Object> source = next.getSourceAsMap();
                String json = JSONObject.toJSONString(source);
                list.add(JSONObject.parseObject(json,mapperClass));
            }
        }
        return list;
    }

    private SearchRequestBuilder sortBuilder(SearchRequestBuilder searchBuilder,String sort){
        //排序
        if(StringUtils.isNotBlank(sort)){
            String[] sp = sort.split(",");
            for (String var:
                    sp) {
                String[] var1 = var.split(" ");
                if(var1.length==1){
                    searchBuilder.addSort(var1[0],SortOrder.ASC);
                }else {
                    searchBuilder.addSort(var1[0],SortOrder.valueOf(var1[1].toUpperCase()));
                }
            }
        }
        return searchBuilder;
    }

    protected LocalDateTime getTodayStart(){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startTime = localDateTime.minusHours(localDateTime.getHour()).minusMinutes(localDateTime.getMinute()).minusSeconds(localDateTime.getSecond());
        return startTime;
    }

    protected LocalDateTime getTodayEnd(){
        LocalDateTime endTime = getTodayStart().plusHours(23).plusMinutes(59).plusSeconds(59);
        return endTime;
    }
}
