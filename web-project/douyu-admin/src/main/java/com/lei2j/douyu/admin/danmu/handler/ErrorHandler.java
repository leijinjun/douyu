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
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/20.
 */
@Component
public class ErrorHandler extends AbstractMessageHandler {

    @Override
    protected void afterSetHandler() {
        if(!HANDLER_MAP.containsKey(MessageType.ERROR)){
            HANDLER_MAP.put(MessageType.ERROR, this);
        }
    }

    @Override
    public void handle(Map<String, Object> messageMap, DouyuLogin douyuLogin) {
        logger.error("房间|{},服务器返回错误",douyuLogin.getRoom());
        douyuLogin.retry();
    }
}
