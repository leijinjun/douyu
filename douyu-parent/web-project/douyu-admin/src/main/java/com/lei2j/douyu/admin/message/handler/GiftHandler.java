package com.lei2j.douyu.admin.message.handler;

import com.lei2j.douyu.danmu.service.DouyuLogin;
import com.lei2j.douyu.danmu.service.MessageType;
import com.lei2j.douyu.service.impl.es.GiftIndex;
import com.lei2j.douyu.vo.GiftVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.apache.commons.beanutils.BeanUtils;
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
    public void handler(Map<String, Object> messageMap, DouyuLogin douyuLogin) throws Exception {
        String gfid = String.valueOf(messageMap.get("gfid"));
        //礼物列表
        Map<Integer, RoomGiftVo> roomGift = douyuLogin.getRoomGift();
        Integer var3 = Integer.valueOf(gfid);
        RoomGiftVo roomGiftVo = roomGift.get(var3);
        //过滤免费礼物
        if (!roomGift.containsKey(var3) || roomGiftVo.getType() == 1) {
            return;
        }
        GiftVo giftVO = new GiftVo();
        BeanUtils.populate(giftVO, messageMap);
        giftVO.setPc(roomGiftVo.getPc().doubleValue());
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
