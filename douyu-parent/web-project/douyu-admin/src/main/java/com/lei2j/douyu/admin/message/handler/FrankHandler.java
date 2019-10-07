package com.lei2j.douyu.admin.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.pojo.FrankEntity;
import com.lei2j.douyu.service.FrankService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author lei2j
 */
@Component
public class FrankHandler extends AbstractMessageHandler{

    @Resource
    private FrankService frankService;

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ROOM_FANS_LSIT)){
            HANDLER_MAP.put(MessageType.ROOM_FANS_LSIT, this);
        }
    }

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        FrankEntity frank = JSONObject.parseObject(JSONObject.toJSONString(messageMap), FrankEntity.class);
        frank.setTop10(String.valueOf(messageMap.get("list")));
        frankService.addFrank(frank);
    }
}
