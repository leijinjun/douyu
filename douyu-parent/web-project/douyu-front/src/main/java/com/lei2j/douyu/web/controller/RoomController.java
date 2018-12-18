package com.lei2j.douyu.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.cache.CacheRoomService;
import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.es.search.ChatSearchService;
import com.lei2j.douyu.es.search.GiftSearchService;
import com.lei2j.douyu.es.search.SearchPage;
import com.lei2j.douyu.login.service.DouyuLogin;
import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.qo.ChatQuery;
import com.lei2j.douyu.qo.FrankQuery;
import com.lei2j.douyu.qo.GiftQuery;
import com.lei2j.douyu.qo.NobleQuery;
import com.lei2j.douyu.service.FrankService;
import com.lei2j.douyu.service.NobleService;
import com.lei2j.douyu.util.BeanUtils;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.vo.*;
import com.lei2j.douyu.web.response.Pagination;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.util.DouyuUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@RestController
@RequestMapping("/room")
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
     * 获取直播列表
     * @param cateId 分类id
     * @return
     */
    @GetMapping("/list")
    public Response getRoomList(@RequestParam(value = "cate",required = false)String cateId){
        List<RoomVo> list = new LinkedList<>();
        String url;
        if(StringUtils.isNotBlank(cateId)){
            url = DouyuApi.ROOM_CATE_API.replace("{cateId}",cateId).replace("{page}","1");
        }else {
            url = DouyuApi.ROOM_ALL_API.replace("{page}","1");
        }
        String s = HttpUtil.get(url);
        JSONObject var1 = JSONObject.parseObject(s);
        String codeKey = "code";
        if(var1.getIntValue(codeKey)==0){
            JSONArray jsonArray = var1.getJSONObject("data").getJSONArray("rl");
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                int ot = jsonObject.getIntValue("ot");
                if(ot==0){
                    Integer roomId = jsonObject.getInteger("rid");
                    String roomSrc = jsonObject.getString("rs16");
                    String roomName = jsonObject.getString("rn");
                    Integer hn = jsonObject.getInteger("ol");
                    String nickname = jsonObject.getString("nn");
                    RoomVo roomVO = new RoomVo();
                    roomVO.setRoomId(roomId);
                    roomVO.setRoomSrc(roomSrc);
                    roomVO.setRoomName(roomName);
                    roomVO.setHn(hn);
                    roomVO.setNickname(nickname);
                    if(cacheRoomService.get(roomId)!=null) {
                        roomVO.setConnected(true);
                    }
                    list.add(roomVO);
                }
            }
        }
        list.sort((v1,v2)->{
        	int b1=v1.isConnected()!=null&&v1.isConnected()?1:-1;
        	int b2=v2.isConnected()!=null&&v2.isConnected()?1:-1;
        	return b1-b2>0?-1:b1-b2==0?0:1;
        });
        return Response.ok().entity(list);
    }

    /**
     * 根据房间Id获取直播间综合信息
     * @param roomId 房间Id
     * @return
     */
    @GetMapping("/info/{roomId}")
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
        responseMap.put("connected",cacheRoomService.get(roomId)==null?false:true);
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
    @GetMapping("/detail/{roomId}")
    public Response roomDetail(@PathVariable("roomId")Integer roomId){
        RoomDetailVo roomDetail = DouyuUtil.getRoomDetail(roomId);
        RoomVo roomVO = new RoomVo();
        roomVO.setRoomId(roomDetail.getRoomId());
        roomVO.setRoomName(roomDetail.getRoomName());
        roomVO.setRoomSrc(roomDetail.getRoomThumb());
        roomVO.setHn(roomDetail.getHn());
        roomVO.setNickname(roomDetail.getOwnerName());
        if(cacheRoomService.get(roomId)!=null){
            roomVO.setConnected(true);
        }
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
    @GetMapping("/danmu/{roomId}")
    public Response getDanmuListByRoom(@PathVariable("roomId")String roomId, @RequestParam(value="from",required=false,defaultValue="0")Integer from
                        , @RequestParam(value = "size",defaultValue = "100")Integer size, ChatQuery chatQO){
        SearchPage searchPage = new SearchPage();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("rid", roomId));
        if(StringUtils.isNotBlank(chatQO.getNn())){
            queryBuilder.must(QueryBuilders.termQuery("nn",chatQO.getNn()));
        }
        if(chatQO.getUid()!=null) {
        	queryBuilder.must(QueryBuilders.termQuery("uid", chatQO.getUid()));
        }
        if(chatQO.getStart()!=null&&chatQO.getEnd()!=null) {
        	queryBuilder.must(QueryBuilders.rangeQuery("createAt")
        			.from(DateUtil.localDateTimeFormat(chatQO.getStart()), true)
        			.to(DateUtil.localDateTimeFormat(chatQO.getEnd()), true))
        			;
        }
        if(chatQO.getStart()!=null&&chatQO.getEnd()==null) {
        	queryBuilder.must(QueryBuilders.rangeQuery("createAt")
        			.from(DateUtil.localDateTimeFormat(chatQO.getStart()), true));
        }
        if(chatQO.getEnd()!=null&&chatQO.getStart()==null){
        	queryBuilder.must(QueryBuilders.rangeQuery("createAt")
        			.to(DateUtil.localDateTimeFormat(chatQO.getEnd()), true));
        }
        searchPage.setQueryBuilder(queryBuilder);
        searchPage.setSort("createAt DESC");
        Pagination<ChatMessageVo,SearchPage> pagination = new Pagination<>(size,from/size+1,searchPage);
        pagination = chatSearchService.query(pagination);
        Map<String,Object> data = new HashMap<>(3);
        data.put("isMore",pagination.getPageNum()<pagination.getTotalPage());
        data.put("pageNum",pagination.getPageNum());
        data.put("chats",pagination.getItems());
        return Response.ok().entity(data);
    }

    /**
     * 获取已连接房间列表
     * @return
     */
    @GetMapping("/logged")
    public Response getLoggedRooms(){
        String result = HttpUtil.get(DouyuApi.ROOM_ALL_API.replace("{page}","1"));
        Map<Integer,RoomVo> resultMap = new HashMap<>(80);
        JSONObject var1 = JSONObject.parseObject(result);
        String codeKey = "code";
        if(var1.getIntValue(codeKey)==0){
            JSONArray jsonArray = var1.getJSONObject("data").getJSONArray("rl");
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                int ot = jsonObject.getIntValue("ot");
                if(ot==0){
                    Integer roomId = jsonObject.getInteger("rid");
                    String roomSrc = jsonObject.getString("rs16");
                    String roomName = jsonObject.getString("rn");
                    Integer hn = jsonObject.getInteger("ol");
                    String nickname = jsonObject.getString("nn");
                    RoomVo roomVO = new RoomVo();
                    roomVO.setRoomId(roomId);
                    roomVO.setRoomSrc(roomSrc);
                    roomVO.setRoomName(roomName);
                    roomVO.setHn(hn);
                    roomVO.setNickname(nickname);
                    if(cacheRoomService.get(roomId)!=null) {
                        roomVO.setConnected(true);
                        resultMap.put(roomId,roomVO);
                    }
                }
            }
        }
        Map<Integer, DouyuLogin> map = cacheRoomService.getAll();
        Set<Integer> set = new HashSet<>(map.keySet());
        set.removeAll(resultMap.keySet());
        List<RoomVo> list = new ArrayList<>();
        list.addAll(resultMap.values());
        set.forEach((room)->{
            RoomDetailVo roomDetail = DouyuUtil.getRoomDetail(room);
            RoomVo roomVO = BeanUtils.copyProperties(roomDetail,RoomVo.class);
            roomVO.setConnected(true);
            roomVO.setNickname(roomDetail.getOwnerName());
            roomVO.setRoomSrc(roomDetail.getRoomThumb());
            list.add(roomVO);
        });
        return Response.ok().entity(list);
    }

    /**
     * 获取所有直播分类列表
     * @return
     * @throws IOException
     */
    @GetMapping("/cates")
    public Response getAllCate()throws IOException {
        String url = "https://www.douyu.com/directory";
        Element element = Jsoup.connect(url).get().body().getElementById("live-list-contentbox");
        Elements lis = element.select("li");
        List<CateVo> list = new ArrayList<>();
        lis.forEach((var)->{
            CateVo cate = new CateVo();
            Element a = var.getElementsByTag("a").get(0);
            Integer cateId = Integer.parseInt(a.attr("data-tid"));
            String href = a.attr("href");
            String alias = href;
            String startStr = "/g_";
            if(href.startsWith(startStr)){
                alias = href.substring(2);
            }
            Element img = a.getElementsByTag("img").get(0);
            String src = img.attr("data-original");
            Element p = a.getElementsByTag("p").get(0);
            String cateName = p.text();
            cate.setCateId(cateId);
            cate.setAlias(alias);
            cate.setCateName(cateName);
            cate.setImg(src);
            list.add(cate);
        });
        return  Response.ok().entity(list);
    }

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
        nobles.forEach((var)->{
            xAxis.add(var.getCreateAt());
            yAxis.add(var.getNum());
        });
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
