package com.lei2j.douyu.admin.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.qo.*;
import com.lei2j.douyu.service.FrankService;
import com.lei2j.douyu.service.NobleService;
import com.lei2j.douyu.service.es.ChatSearchService;
import com.lei2j.douyu.service.es.GiftSearchService;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.vo.GiftVo;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.vo.RoomVo;
import com.lei2j.douyu.web.response.Pagination;
import com.lei2j.douyu.web.response.Response;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@RestController
@RequestMapping("/room/info")
public class RoomAdminController extends BaseController {

    @Resource
    private CacheRoomService cacheRoomService;
    @Resource
    private GiftSearchService giftSearchService;
    @Resource
    private ChatSearchService chatSearchService;
    @Resource
    private NobleService nobleService;
    @Resource
    private FrankService frankService;

    /**
     * 获取直播列表
     * @param cateId 分类标识
     * @param limit
     * @return
     */
    @GetMapping("/list")
    public Response getRoomList(@RequestParam(value = "cate",required = false)String cateId,
                                @RequestParam(value = "limit",required = false,defaultValue = "100") Integer limit){
        LinkedList<RoomVo> list = new LinkedList<>();
        StringBuilder url = new StringBuilder(DouyuApi.ROOM_ALL_API);
        if(!StringUtils.isEmpty(cateId)){
            url.append("/").append(cateId);
        }
        url.append("?limit=").append(limit);
        String s = HttpUtil.get(url.toString());
        JSONObject var1 = JSONObject.parseObject(s);
        String codeKey = "error";
        if(var1.getIntValue(codeKey)==0){
            JSONArray jsonArray = var1.getJSONArray("data");
            for (Object jsonObject:
                 jsonArray) {
                    RoomVo roomVo = JSONObject.toJavaObject((JSONObject) jsonObject, RoomVo.class);
                    boolean f = cacheRoomService.containsKey(roomVo.getRoomId());
                    roomVo.setConnected(f);
                    if(f){
                        list.addFirst(roomVo);
                    }else {
                        list.add(roomVo);
                    }
            }
        }
        return Response.ok().entity(list);
    }

    /**
     * 根据房间Id获取直播间综合信息
     * @param roomId 房间Id
     * @return
     */
    @GetMapping("/{roomId:\\d+}")
    public Response roomInfo(@PathVariable("roomId")Integer roomId) {
        RoomDetailVo roomDetailVO = DouyuUtil.getRoomDetail(roomId);
        LocalDateTime startToDay = getStartToDay();
        LocalDateTime endToDay = getEndToDay();
        String start = DateUtil.localDateTimeFormat(startToDay);
        String end = DateUtil.localDateTimeFormat(endToDay);
        SearchPage giftSearchPage = new SearchPage();
        giftSearchPage.setQueryBuilder(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("rid", roomId))
                        .must(QueryBuilders.rangeQuery("createAt")
                                .from(start,true)
                                .to(end,true)
                        )
        );
        giftSearchPage.setFilterBuilder(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("pc").gte(6)));
        giftSearchPage.setSort("createAt DESC,pc DESC");
        Pagination<GiftVo,SearchPage> giftPagination = new Pagination<>(10,1,giftSearchPage);
        Pagination<GiftVo,SearchPage> voPagination = giftSearchService.query(giftPagination);
        SearchPage chatSearchPage = new SearchPage();
        chatSearchPage.setQueryBuilder(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("rid", roomId))
                        .must(QueryBuilders.rangeQuery("createAt")
                                .from(start,true)
                                .to(end,true)
                        )
        );
        chatSearchPage.setSort("createAt DESC");
        Pagination<ChatMessageVo,SearchPage> pagination = new Pagination<>(20,1,chatSearchPage);
        pagination = chatSearchService.query(pagination);
        BigDecimal giftSum = giftSearchService.getToDayGiftSumAggregationByRoom(roomId);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String sum = decimalFormat.format(giftSum);
        Integer giftUserCounts = giftSearchService.getToDayGiftUserCountsAggregationByRoom(roomId);
        Integer userCounts = chatSearchService.getToDayUserCountsAggregationnByRoom(roomId);
        GiftQuery giftQO = new GiftQuery();
        giftQO.setRid(roomId);
    	giftQO.setStart(getStartToDay());
    	giftQO.setEnd(getEndToDay());
		List<Map<String, Object>> giftTopList = giftSearchService.getToDayGiftTopSum(giftQO);
        Map<String,Object> aggregateMap = new HashMap<>(3);
        aggregateMap.put("giftSum",sum);
        aggregateMap.put("userCounts",userCounts);
        aggregateMap.put("giftUserCounts",giftUserCounts);
        Map<String, Object> responseMap = new HashMap<>(5);
        responseMap.put("roomDetail", roomDetailVO);
        responseMap.put("gifts", voPagination.getItems());
        responseMap.put("chats", pagination.getItems());
        responseMap.put("connected",cacheRoomService.containsKey(roomId));
        responseMap.put("chatTotalCount",pagination.getTotal());
        responseMap.put("aggregate",aggregateMap);
        responseMap.put("giftTop", giftTopList);
    	return Response.ok().entity(responseMap);
    }

    /**
     * 获取单个房间详细信息
     * @param roomId
     * @return
     */
    @GetMapping("/detail/{roomId:\\d+}")
    public Response roomDetail(@PathVariable("roomId")Integer roomId){
        RoomDetailVo roomDetail = DouyuUtil.getRoomDetail(roomId);
        RoomVo roomVO = new RoomVo();
        roomVO.setRoomId(roomDetail.getRoomId());
        roomVO.setRoomName(roomDetail.getRoomName());
        roomVO.setRoomSrc(roomDetail.getRoomThumb());
        roomVO.setHn(roomDetail.getHn());
        roomVO.setNickname(roomDetail.getOwnerName());
        roomVO.setConnected(cacheRoomService.containsKey(roomId));
        Map<String,Object> data = new HashMap<>(1);
        data.put("room",roomVO);
        return Response.ok().entity(data);
    }

    /**
     * 今日开始时间
     * @return
     */
    private LocalDateTime getStartToDay() {
    	LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startTime = localDateTime.minusHours(localDateTime.getHour()).minusMinutes(localDateTime.getMinute()).minusSeconds(localDateTime.getSecond());
        return startTime;
        
    }
    
    /**
     * 今日结束时间
     * @return
     */
    private LocalDateTime getEndToDay() {
    	LocalDateTime startToDay = getStartToDay();
    	LocalDateTime endTime = startToDay.plusHours(23).plusMinutes(59).plusSeconds(59);
    	return endTime;
    }
}
