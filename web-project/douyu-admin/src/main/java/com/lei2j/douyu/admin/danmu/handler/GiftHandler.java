/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.admin.danmu.handler;

import com.lei2j.douyu.admin.danmu.config.MessageType;
import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import com.lei2j.douyu.service.impl.es.GiftIndex;
import com.lei2j.douyu.vo.GiftVo;
import com.lei2j.douyu.vo.RoomGiftVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
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
    public void handle(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
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
        try {
            BeanUtils.populate(giftVO, messageMap);
            giftVO.setPc(roomGiftVo.getPc().doubleValue());
            giftVO.setGiftName(roomGiftVo.getName());
            LocalDateTime now = LocalDateTime.now(ZoneId.of("+8"));
            giftVO.setCreateAt(now);
            giftVO.setId(UUID.randomUUID().toString().replaceAll("-","")+now.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            giftIndex.createDocument(giftVO.getId(),giftVO);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void afterSetHandler() {
        if (!HANDLER_MAP.containsKey(MessageType.GIVE_GIFT)) {
            HANDLER_MAP.put(MessageType.GIVE_GIFT, this);
        }
    }
}
