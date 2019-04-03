package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.cache.CacheRoomService;
import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.qo.*;
import com.lei2j.douyu.service.FrankService;
import com.lei2j.douyu.service.NobleService;
import com.lei2j.douyu.service.es.ChatSearchService;
import com.lei2j.douyu.service.es.GiftSearchService;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.*;
import com.lei2j.douyu.web.response.Pagination;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.view.DanmuRankingListView;
import com.lei2j.douyu.web.view.GiftRankingListView;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@RestController
@RequestMapping("/room/info")
public class RoomController extends BaseController {

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
     * 房间弹幕列表
     * @param roomId 房间Id
     * @param from 起始位置
     * @param size 查询条数
     * @param chatQO 查询条件对象
     * @return
     */
//    @GetMapping("/danmu/{roomId:\\d+}")
//    public Response getDanmuListByRoom(@PathVariable("roomId")String roomId, @RequestParam(value="from",required=false,defaultValue="0")Integer from
//                        , @RequestParam(value = "size",defaultValue = "100")Integer size, ChatQuery chatQO){
//        SearchPage searchPage = new SearchPage();
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("rid", roomId));
//        if(!StringUtils.isEmpty(chatQO.getNn())){
//            queryBuilder.must(QueryBuilders.termQuery("nn",chatQO.getNn()));
//        }
//        if (chatQO.getUid() != null) {
//            queryBuilder.must(QueryBuilders.termQuery("uid", chatQO.getUid()));
//        }
//        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("createAt");
//        LocalDateTime startTime = chatQO.getStart();
//        LocalDateTime endTime = chatQO.getEnd();
//        if (startTime != null) {
//            rangeQuery.from(DateUtil.localDateTimeFormat(startTime), true);
//        }
//        if (endTime != null) {
//            rangeQuery.to(DateUtil.localDateTimeFormat(endTime), true);
//        }
//        if (rangeQuery.format() != null || rangeQuery.to() != null) {
//            queryBuilder.must(rangeQuery);
//        }
//        searchPage.setQueryBuilder(queryBuilder);
//        searchPage.setSort("createAt DESC");
//        Pagination<ChatMessageVo,SearchPage> pagination = new Pagination<>(size,from/size+1,searchPage);
//        pagination = chatSearchService.query(pagination);
//        Map<String,Object> data = new HashMap<>(3);
//        data.put("isMore",pagination.getPageNum()<pagination.getTotalPage());
//        data.put("pageNum",pagination.getPageNum());
//        data.put("chats",pagination.getItems());
//        return Response.ok().entity(data);
//    }

    /**
     * 获取当天贵族视图数据
     * @param room
     * @return
     */
    @GetMapping("/viewNoble/{room}")
    public Response viewNobleData(@PathVariable("room") Integer room,
                                  @RequestParam("start") LocalDateTime start, @RequestParam("end") LocalDateTime end){
        NobleQuery nobleQO = new NobleQuery();
        nobleQO.setRid(room);
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startTime = localDateTime.minusHours(localDateTime.getHour()).minusMinutes(localDateTime.getMinute()).minusSeconds(localDateTime.getSecond());
        LocalDateTime endTime = startTime.plusHours(23).plusMinutes(59).plusSeconds(59);
        nobleQO.setStart(startTime);
        nobleQO.setEnd(endTime);
        List<NobleEntity> nobles = nobleService.findByCondition(nobleQO);
        List<Object> xAxis = new ArrayList<>();
        List<Object> yAxis = new ArrayList<>();
        for (NobleEntity var:
             nobles) {
            xAxis.add(var.getCreateAt());
            yAxis.add(var.getNum());
        }
        Map<String,Object> dataMap = new HashMap<>(2);
        dataMap.put("xAxis",xAxis);
        dataMap.put("yAxis",yAxis);
        return Response.ok().entity(dataMap);
    }

    /**
     * 统计粉丝人数
     * @param frankQO
     * @return
     */
    @RequestMapping("/view/fansPersonNum/{room}")
    public Response viewFansPersonNumData(@PathVariable("room")Integer rid, FrankQuery frankQO){
        frankQO.setRid(rid);
        LocalDateTime start = frankQO.getStart();
        LocalDateTime end = frankQO.getEnd();
        List<Object[]> mapList = frankService.findStatisticByTimes(frankQO);
        Set<String> set = new HashSet<>(30);
        mapList.forEach((obj)->set.add((String)obj[0]));
        for (LocalDateTime i=start;i.isBefore(end);){
            String format = DateUtil.localDateTimeFormat(i, DateFormatConstants.DATE_FORMAT);
            if(!set.contains(format)){
                Object[] obj = new Object[2];
                obj[0] = format;
                obj[1] = 0;
                mapList.add(obj);
            }
            i = i.plusDays(1);
        }
        mapList.sort((o1, o2) -> {
            String key1 = (String) o1[0];
            String key2 = (String) o2[0];
            LocalDate d1 = LocalDate.parse(key1);
            LocalDate d2 = LocalDate.parse(key2);
            return d1.compareTo(d2);
        });
        return Response.ok().entity(mapList);
    }

    /**
     * 统计礼物总数
     * @param giftQO
     * @return
     */
    @RequestMapping("/view/giftMoney/{room}")
    public Response viewGiftMoneyData(@PathVariable("room")Integer rid, GiftQuery giftQO){
        giftQO.setRid(rid);
        Map<String,Object> giftDataMap = giftSearchService.getGiftSumIntervalDayByRoom(giftQO);
        return Response.ok().entity(giftDataMap);
    }

    /**
     * 统计礼物人数
     * @param giftQO
     * @return
     */
    @RequestMapping("/view/giftPersonNum/{room}")
    public Response viewGiftPersonNum(@PathVariable("room")Integer rid, GiftQuery giftQO){
        giftQO.setRid(rid);
        Map<String,Integer> giftCountsMap = giftSearchService.getIntervalDayPersonCountsByRoom(giftQO);
        return Response.ok().entity(giftCountsMap);
    }

    /**
     * 统计弹幕条数
     * @param chatQO
     * @return
     */
    @RequestMapping("/view/chatSum/{room}")
    public Response viewChatSumData(@PathVariable("room")Integer rid, ChatQuery chatQO){
        chatQO.setRid(rid);
        Map<String,Integer> dataMap = chatSearchService.getIntervalDayChatSumByRoom(chatQO);
        return Response.ok().entity(dataMap);
    }

    /**
     * 统计弹幕人数
     * @param chatQO
     * @return
     */
    @RequestMapping("/view/chatPersonNum/{room}")
    public Response viewChatPersonNumData(ChatQuery chatQO){
        Map<String,Integer> dataMap = chatSearchService.getIntervalDayChatPersonCountsByRoom(chatQO);
        return Response.ok().entity(dataMap);
    }
    
    /**
     * 每日礼物土豪榜
     * @param giftQO
     * @return
     */
    @GetMapping("/view/giftTopSum/{room}")
    public Response viewGiftTopSumData(@PathVariable("room") Integer room, GiftQuery giftQO) {
    	giftQO.setRid(room);
    	giftQO.setStart(getStartToDay());
    	giftQO.setEnd(getEndToDay());
    	List<Map<String, Object>> list = giftSearchService.getToDayGiftTopSum(giftQO);
    	return Response.ok().entity(list);
    }

    /*@GetMapping("/view/{roomId}")
    public response viewRoom(@PathVariable("roomId")Integer room){
        logger.info("参数room:{}",room);
        FrankQO frankQO = new FrankQO();
        frankQO.setRid(room);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LocalDateTime localNow = now.toLocalDateTime();
        LocalDateTime startTime = localNow.minusHours(localNow.getHour()).minusMinutes(localNow.getMinute()).minusSeconds(localNow.getSecond());
        startTime = startTime.minusDays(startTime.getDayOfWeek().getValue()-1);
        LocalDateTime endTime = startTime.plusDays(6).plusHours(23).plusMinutes(59).plusSeconds(59);
        frankQO.setStartTime(startTime);
        frankQO.setEndTime(endTime);
        List<FrankViewVO> frankViewQOS = frankService.findByCondition(frankQO);
        int capacity = frankViewQOS.size();
        List<LocalDate> frankViewX = new ArrayList<>(capacity);
        List<Integer> frankViewY = new ArrayList<>(capacity);
        frankViewQOS.forEach((var)->{
            frankViewX.add(var.getCurrentDate());
            frankViewY.add(var.getFc());
        });
        frankViewX.sort((var1,var2)->{ return var1.compareTo(var2);});
        LocalDateTime currentDayStart = localNow.minusHours(localNow.getHour()).minusMinutes(localNow.getMinute()).minusSeconds(localNow.getSecond());
        LocalDateTime currentDateEnd = currentDayStart.plusHours(23).plusMinutes(59).plusSeconds(59);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("rid",room))
                .must(QueryBuilders.rangeQuery("timestamp")
                        .from(currentDayStart.toInstant(ZoneOffset.of("+8")).toEpochMilli(),true)
                        .to(currentDateEnd.toInstant(ZoneOffset.of("+8")).toEpochMilli(),true)
                )
                .must(QueryBuilders.regexpQuery("txt",".{4,}"));
        SearchPage searchPage = new SearchPage(queryBuilder,null,true,null);
        Pagination<ChatMessageVO> pagination = new Pagination<>();
        pagination.putParam(SearchPage.PARAM_KEY,searchPage);
        pagination = chatSearchService.query(pagination);
        List<ChatMessageVO> items = pagination.getItems();
        Map<String,Integer> cloudMap = new HashMap<>(items.size()/3);
        long start = System.currentTimeMillis();
        logger.info("开始处理弹幕数据...");
        items.parallelStream().forEach((item)->{
            String txt = item.getTxt();
            StringReader reader = new StringReader(txt);
            IKSegmenter ikSegmenter = new IKSegmenter(reader,true);
            try {
                Lexeme lexeme;
                while ((lexeme=ikSegmenter.next())!=null){
                    String lexemeText = lexeme.getLexemeText();
                    if(StringUtils.isNotBlank(lexemeText)&&lexemeText.length()>=2&&lexemeText.matches("^[\\u4E00-\\u9FA5]+$")){
                        if(cloudMap.containsKey(lexemeText)){
                            Integer count = cloudMap.get(lexemeText);
                            cloudMap.put(lexemeText,++count);
                        }else{
                            cloudMap.put(lexemeText,1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        logger.info("弹幕数据处理完毕");
        logger.info("弹幕数据处理时间:{}s",(end-start)/1000);
        HashMap<Object, Object> frankMap = new HashMap<>(2);
        frankMap.put("frankViewX",frankViewX);
        frankMap.put("frankViewY",frankViewY);
        Map<String,Object> responseMap = new HashMap<>(2);
        responseMap.put("frankView",frankMap);
        responseMap.put("clouds",cloudMap);
        return response.ok().entity(responseMap);
    }*/

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
