package com.lei2j.douyu.job;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.cache.CacheRoomService;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.login.service.DouyuLogin;
import com.lei2j.douyu.login.service.DouyuNioLogin;
import com.lei2j.douyu.login.service.DouyuNormalLogin;
import com.lei2j.douyu.pojo.RoomConnectEntity;
import com.lei2j.douyu.qo.RoomConnectQuery;
import com.lei2j.douyu.service.RoomConnectService;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.web.response.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author lei2j
 */
@Component
@EnableScheduling
public class DouyuConnectJob extends DouyuJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(DouyuConnectJob.class);

    public DouyuConnectJob(){
    }

    @Resource
    private CacheRoomService cacheRoomService;

    @Resource
    private RoomConnectService roomConnectService;

    /**
     * 定时任务，连接指定房间弹幕服务器
     */
    public void connectRooms(){
        long st = System.currentTimeMillis();
        LOGGER.info(">>>>>>>>>>>>>>start connect room");
        Integer pageNum = 1;
        Integer limit = 50;
        RoomConnectQuery roomConnectQuery = new RoomConnectQuery();
        roomConnectQuery.setConnect(1);
        Pagination<RoomConnectEntity,RoomConnectQuery> pagination = new Pagination<>();
        pagination.setLimit(limit);
        pagination.setPageNum(pageNum);
        pagination.setParams(roomConnectQuery);
        Pagination<RoomConnectEntity, RoomConnectQuery> pageByCondition = roomConnectService.getPageByCondition(pagination);
        List<RoomConnectEntity> items = pageByCondition.getItems();
        while (!CollectionUtils.isEmpty(items)){
            items.forEach((item)->{
                Integer roomId = item.getRoomId();
                if(cacheRoomService.containsKey(roomId)){
                   return;
                }
                String url = DouyuApi.ROOM_DETAIL_API.replace("{room}",String.valueOf(roomId));
                String jsonStr = HttpUtil.get(url,null);
                JSONObject jsonObj = JSONObject.parseObject(jsonStr);
                String errorKey = "error";
                if(jsonObj.getIntValue(errorKey)==0) {
                    JSONObject dataObj = jsonObj.getJSONObject("data");
                    int roomStatus = dataObj.getIntValue("room_status");
                    if(roomStatus==1){
                        int hn = dataObj.getInteger("hn");
                        Integer hightHn = 1000000;
                        DouyuLogin douyuLogin;
                        try {
                            if (hn >= hightHn) {
                                douyuLogin = new DouyuNormalLogin(roomId);
                            } else {
                                douyuLogin = new DouyuNioLogin(roomId);
                            }
                            LOGGER.info("连接房间:{}", roomId);
                            douyuLogin.login();
                            cacheRoomService.cache(roomId, douyuLogin);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    LOGGER.error("获取房间|{}信息错误",roomId);
                }
            });
            pageNum++;
            pagination.setPageNum(pageNum);
            pageByCondition = roomConnectService.getPageByCondition(pagination);
            items = pageByCondition.getItems();
        }
        long ed = System.currentTimeMillis();
        LOGGER.info(">>>>>>>>>>>>>>end connect room,run time:{}ms",ed-st);
    }

  /**
   * 定时任务，检查已连接房间
   * */
  public void checkConnectedRooms() {
      long st = System.currentTimeMillis();
      LOGGER.info(">>>>>>>>>>>>start check connected rooms");
      Map<Integer, DouyuLogin> douyuLoginMap = cacheRoomService.getAll();
      douyuLoginMap.forEach(
              (roomId, douyuLogin) -> {
                  String roomUrl = DouyuApi.ROOM_DETAIL_API.replace("{room}", String.valueOf(roomId));
                  String str = HttpUtil.get(roomUrl, null);
                  JSONObject jsonObject = JSONObject.parseObject(str);
                  if (jsonObject.getIntValue("error") == 0) {
                      JSONObject dataObj = jsonObject.getJSONObject("data");
                      // 未开播
                      if (dataObj.getIntValue("room_status") == 2) {
                    	  LOGGER.info("房间|{},关闭直播",String.valueOf(roomId));
                          douyuLogin.logout();
                      }
                  }
      });
      long ed = System.currentTimeMillis();
      LOGGER.info(">>>>>>>>>>>>end check connected rooms,run time:{}ms",ed-st);
  }
}
