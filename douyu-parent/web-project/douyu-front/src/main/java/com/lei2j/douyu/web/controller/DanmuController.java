package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.qo.DanmuQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.service.es.ChatSearchService;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.vo.DanmuSearchView;
import com.lei2j.douyu.vo.DanmuSearchWithUserView;
import com.lei2j.douyu.web.response.Pagination;
import com.lei2j.douyu.web.response.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * author: lei2j
 * date: 2019/3/31
 */
@RestController
@RequestMapping("/danmu")
public class DanmuController extends BaseController {

    @Autowired
    private ChatSearchService chatSearchService;

    @GetMapping("")
    public Response find(@RequestParam(value = "limit",required = false,defaultValue = "15") Integer limit ,
                         @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                         DanmuQuery danmuQuery){
        String ownerName = danmuQuery.getOwnerName();
        DouyuUtil.SearchRoomInfo searchRoomInfo = null;
        boolean f = false;
        if (!StringUtils.isEmpty(ownerName)) {
            Optional<DouyuUtil.SearchRoomInfo> roomInfoOptional = DouyuUtil.search(ownerName);
            if(!roomInfoOptional.isPresent()){
                return Response.ok();
            }
            searchRoomInfo = roomInfoOptional.get();
            f = true;
        }
        String roomId = f ? searchRoomInfo.getRoomId() : danmuQuery.getRoomId();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(roomId)){
            boolQueryBuilder.should(QueryBuilders.termQuery("rid", roomId));
        }
        if(!StringUtils.isEmpty(roomId)){
            boolQueryBuilder.should(QueryBuilders.termsQuery("nn", danmuQuery.getNn()));
        }
        if(danmuQuery.getStartDate()!=null){
            boolQueryBuilder.must(
                    QueryBuilders.rangeQuery("createAt")
                            .format(DateFormatConstants.DATE_FORMAT)
                            .from(LocalDateTime.of(danmuQuery.getStartDate(), LocalTime.MIN),true));
        }
        if(danmuQuery.getEndDate()!=null){
            boolQueryBuilder.must(
                    QueryBuilders.rangeQuery("createAt")
                            .format(DateFormatConstants.DATETIME_FORMAT)
                            .to(LocalDateTime.of(danmuQuery.getEndDate(), LocalTime.MAX), true));
        }
        QueryBuilder queryBuilder = boolQueryBuilder;
        SearchPage searchPage = new SearchPage(queryBuilder,null);
        searchPage.setSort("createAt desc");
        Pagination<DanmuSearchView,SearchPage> pagination = new Pagination<>(limit,pageNum,searchPage);
        pagination = chatSearchService.queryDanmuByCondition(pagination);
        List<DanmuSearchView> items = pagination.getItems();
        if(!CollectionUtils.isEmpty(items)){
            for (DanmuSearchView item :
                    items) {
                item.setOwnerName(ownerName);
            }
        }
        return Response.ok().entity(pagination);
    }

    @GetMapping("/nn")

    public Response findByNn(@RequestParam(value = "limit",required = false,defaultValue = "15") Integer limit ,
                             @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "nn") String nn,
                             @RequestParam(value = "startDate",required = false)LocalDate startDate,
                             @RequestParam(value = "endDate",required = false)LocalDate endDate){

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("nn",nn));
        if (startDate != null) {
            queryBuilder = queryBuilder.must(
                        QueryBuilders.rangeQuery("createAt")
                                .format(DateFormatConstants.DATE_FORMAT)
                                .from(LocalDateTime.of(startDate, LocalTime.MIN),true));
        }
        if (endDate != null) {
            queryBuilder = queryBuilder.must(
                    QueryBuilders.rangeQuery("createAt")
                            .format(DateFormatConstants.DATETIME_FORMAT)
                            .to(LocalDateTime.of(startDate, LocalTime.MAX), true));
        }
        SearchPage searchPage = new SearchPage(queryBuilder,null);
        searchPage.setSort("createAt desc");
        Pagination<DanmuSearchWithUserView,SearchPage> pagination = new Pagination<>(limit,pageNum,searchPage);
        pagination = chatSearchService.queryDanmuWithUserByCondition(pagination);
        return Response.ok().entity(pagination);
    }
}
