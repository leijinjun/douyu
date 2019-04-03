package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.service.es.GiftSearchService;
import com.lei2j.douyu.util.DouyuUtil;
import com.lei2j.douyu.vo.RoomDetailVo;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.view.GiftRankingListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: leijinjun
 * date: 2019/4/3
 */

@RestController
@RequestMapping("/gift")
public class GiftController extends BaseController {

    @Autowired
    private GiftSearchService giftSearchService;

    @GetMapping("/view/today/rankingList")
    public Response viewTodayGiftRankingList(@RequestParam(value = "topSize",defaultValue = "10")Integer topSize){
        Map<String, BigDecimal> sumAggregation = giftSearchService.getToDayGiftByRoomId(topSize);
        if(sumAggregation==null){
            return Response.INTERNAL_SERVER_ERROR;
        }
        List<GiftRankingListView> list = new ArrayList<>(sumAggregation.size());
        for (Map.Entry<String,BigDecimal> entry:
                sumAggregation.entrySet()) {
            String roomId = entry.getKey();
            RoomDetailVo roomDetailVo = DouyuUtil.getRoomDetail(Integer.valueOf(roomId));
            BigDecimal value = entry.getValue();
            GiftRankingListView giftRankingListView = new GiftRankingListView();
            giftRankingListView.setRoomId(roomId);
            giftRankingListView.setGiftMoney(value.setScale(2, RoundingMode.HALF_UP));
            giftRankingListView.setNickName(roomDetailVo.getOwnerName());
            giftRankingListView.setRoomName(roomDetailVo.getRoomName());
            giftRankingListView.setRoomThumb(roomDetailVo.getRoomThumb());
            list.add(giftRankingListView);
        }
        return Response.ok().entity(list);
    }

    @GetMapping("/view/today/userRankingList")
    public Response getUserGiftRankingList(@RequestParam(value = "topSize",defaultValue = "10")Integer topSize){
        List<Map<String, Object>> mapList = giftSearchService.getTodayGiftMoneyByUserId(topSize);
        if(mapList==null){
            return Response.INTERNAL_SERVER_ERROR;
        }
        return Response.ok().entity(mapList);
    }
}
