package com.lei2j.douyu.admin.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.service.impl.es.GiftIndex;
import com.lei2j.douyu.vo.GiftVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
@Component
public class GiftHandler extends AbstractMessageHandler{

    @Resource
    private GiftIndex giftIndex;

    @Override
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        GiftVo giftVO = JSONObject.parseObject(JSONObject.toJSONString(messageMap), GiftVo.class);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("+8"));
        giftVO.setCreateAt(now);
        giftVO.setId(UUID.randomUUID().toString().replaceAll("-","")+now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        giftIndex.createDocument(giftVO.getId(),giftVO);
    }
    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.GIVE_GIFT)){
            HANDLER_MAP.put(MessageType.GIVE_GIFT, this);
        }
    }
}
