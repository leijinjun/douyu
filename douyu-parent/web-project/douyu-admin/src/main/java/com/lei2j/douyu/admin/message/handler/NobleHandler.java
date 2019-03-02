package com.lei2j.douyu.admin.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.pojo.NobleEntity;
import com.lei2j.douyu.service.NobleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房间贵族列表消息处理器
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@Component
public class NobleHandler extends AbstractMessageHandler{

    private static Map<Integer,Long> filterMap = new ConcurrentHashMap<>();

    /**
     * 时间间隔10min
     */
    private static final long INTERVAL_TIME = 600000L;

    @Resource
    private NobleService nobleService;

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        NobleEntity noble = JSONObject.parseObject(JSONObject.toJSONString(messageMap), NobleEntity.class);
        //限制频次
        long currentTime = System.currentTimeMillis();
        Integer room = douyuLogin.getRoom();
        if(filterMap.containsKey(room)){
            Long lastTime = filterMap.get(room);
            long interval = currentTime-lastTime;
            if(interval<INTERVAL_TIME){
                return;
            }
        }
        filterMap.put(room,currentTime);
        nobleService.addNoble(noble);
    }

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ROOM_NOBLE_LIST)){
            HANDLER_MAP.put(MessageType.ROOM_NOBLE_LIST, this);
        }
    }
}
