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
    public void handle(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        FrankEntity frank = JSONObject.parseObject(JSONObject.toJSONString(messageMap), FrankEntity.class);
        frank.setTop10(String.valueOf(messageMap.get("list")));
        frankService.addFrank(frank);
    }
}
