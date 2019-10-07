package com.lei2j.douyu.admin.job;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.admin.cache.CacheRoomService;
import com.lei2j.douyu.admin.danmu.DouyuWorker;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.pojo.RoomConnectEntity;
import com.lei2j.douyu.qo.RoomConnectQuery;
import com.lei2j.douyu.service.RoomConnectService;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.web.response.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lei2j
 */
@Configuration
@EnableScheduling
@EnableAsync(order = Ordered.HIGHEST_PRECEDENCE)
public class DouyuConnectJob extends DouyuJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(DouyuConnectJob.class);

    public DouyuConnectJob(){
    }

    @Resource
    private CacheRoomService cacheRoomService;

    @Resource
    private RoomConnectService roomConnectService;

    @Resource
    private DouyuWorker douyuWorker;

    /**
     * 定时任务，连接指定房间弹幕服务器
     * cron表达式参数：秒 分 时 日 月 周几
     */
    @Scheduled(cron = "${douyu.room.job.schedule.connect}")
    @Async
    public void connectRooms() {
        long st = System.currentTimeMillis();
        LOGGER.info(">>>>>>>>>>>>>>start connect room job");
        Integer pageNum = 1;
        Integer limit = 50;
        RoomConnectQuery roomConnectQuery = new RoomConnectQuery();
        roomConnectQuery.setConnect(1);
        Pagination<RoomConnectEntity, RoomConnectQuery> pagination = new Pagination<>();
        pagination.setLimit(limit);
        pagination.setPageNum(pageNum);
        pagination.setParams(roomConnectQuery);
        Pagination<RoomConnectEntity, RoomConnectQuery> pageByCondition = roomConnectService.getPageByCondition(pagination);
        List<RoomConnectEntity> items = pageByCondition.getItems();
        while (!CollectionUtils.isEmpty(items)) {
            LOGGER.info("[timedTask.connectRoom]房间列表：{}", items);
            for (RoomConnectEntity item :
                    items) {
                Integer roomId = item.getRoomId();
                if (cacheRoomService.containsKey(roomId)) {
                    LOGGER.info("[timedTask.connectRoom]房间已存在：{}", roomId);
                    continue;
                }
                try {
                    String url = DouyuApi.ROOM_DETAIL_API.replace("{room}", String.valueOf(roomId));
                    String jsonStr = HttpUtil.get(url);
                    JSONObject jsonObj = JSONObject.parseObject(jsonStr);
                    String errorKey = "error";
                    if (jsonObj.getIntValue(errorKey) == 0) {
                        JSONObject dataObj = jsonObj.getJSONObject("data");
                        int roomStatus = dataObj.getIntValue("room_status");
                        if (roomStatus == 1) {
                            int hn = dataObj.getInteger("hn");
                            LOGGER.info("[timedTask.connectRoom]开始连接房间:{}", roomId);
                            if (douyuWorker.login(roomId) == -1) {
                                LOGGER.info("[timedTask.connectRoom]连接房间失败:{}", roomId);
                            }
                        } else {
                            LOGGER.info("[timedTask.connectRoom]该房间未开播：{}", roomId);
                        }
                    } else {
                        LOGGER.error("[timedTask.connectRoom]获取房间|{}信息错误", roomId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pageNum++;
            pagination.setPageNum(pageNum);
            pageByCondition = roomConnectService.getPageByCondition(pagination);
            items = pageByCondition.getItems();
        }
        long ed = System.currentTimeMillis();
        LOGGER.info(">>>>>>>>>>>>>>end connect room job,run time:{}ms", ed - st);
    }

  /**
   * 定时任务，检查已连接房间
   * */
  @Scheduled(cron = "${douyu.room.job.schedule.check}")
  @Async
  public void checkConnectedRooms() {
      long st = System.currentTimeMillis();
      LOGGER.info(">>>>>>>>>>>>start check connected room job");
      Map<Integer, DouyuLogin> douyuLoginMap = cacheRoomService.getAll();
      douyuLoginMap.forEach(
              (roomId, douyuLogin) -> {
                  String roomUrl = DouyuApi.ROOM_DETAIL_API.replace("{room}", String.valueOf(roomId));
                  String str = HttpUtil.get(roomUrl);
                  JSONObject jsonObject = JSONObject.parseObject(str);
                  String errorKey = "error";
                  if (jsonObject.getIntValue(errorKey) == 0) {
                      JSONObject dataObj = jsonObject.getJSONObject("data");
                      // 未开播
                      String closedRoomKey = "room_status";
                      int closedRoomStatus = 2;
                      if (dataObj.getIntValue(closedRoomKey) == closedRoomStatus) {
                    	  LOGGER.info("[timedTask.checkRoom]房间|{},关闭直播",roomId);
                          douyuLogin.logout();
                      }
                  }
      });
      long ed = System.currentTimeMillis();
      LOGGER.info(">>>>>>>>>>>>end check connected room job,run time:{}ms",ed-st);
  }

  @Bean
  public TaskExecutor taskExecutor(){
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setAllowCoreThreadTimeOut(false);
      executor.setCorePoolSize(3);
      executor.setMaxPoolSize(4);
      executor.setQueueCapacity(10);
      executor.setKeepAliveSeconds(60 * 60);
      executor.setDaemon(false);
      executor.setRejectedExecutionHandler((r, t) -> LOGGER.warn("定时任务线程池溢出"));
      executor.setThreadFactory(new DefaultThreadFactory("thd-douyu-job-%s", false, 5));
      return executor;
  }

    /**
     * 定时任务自定义线程池
     */
//    @Configuration
//    public class ScheduleConfig implements SchedulingConfigurer {
//
//        @Override
//        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//            taskRegistrar.setScheduler(new ThreadPoolExecutor(3, 4, 30, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10),
//                    (r, t) -> LOGGER.warn("定时任务线程池溢出"))
//            );
//        }
//    }
}
