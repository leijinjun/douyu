package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.qo.DanmuQuery;
import com.lei2j.douyu.qo.SearchPage;
import com.lei2j.douyu.service.es.ChatSearchService;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.DanmuSearchView;
import com.lei2j.douyu.vo.RoomDetailVo;
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
        String roomId = danmuQuery.getRoomId();
        if (!StringUtils.isEmpty(ownerName)) {
            Optional<DouyuUtil.SearchRoomInfo> roomInfoOptional = DouyuUtil.search(ownerName);
            if(!roomInfoOptional.isPresent()){
                return Response.ok();
            }
            DouyuUtil.SearchRoomInfo searchRoomInfo = roomInfoOptional.get();
            roomId = searchRoomInfo.getRoomId();
        }else if(!StringUtils.isEmpty(roomId)){
            RoomDetailVo roomDetail = DouyuUtil.getRoomDetail(Integer.parseInt(roomId));
            if(roomDetail==null){
                return Response.NOT_FOUND;
            }
            ownerName = roomDetail.getOwnerName();
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(roomId)){
            boolQueryBuilder.should(QueryBuilders.termQuery("rid", roomId));
        }
        if(!StringUtils.isEmpty(danmuQuery.getNn())){
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
}
