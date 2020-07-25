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

import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface MessageHandler {
	
	Map<String,MessageHandler> HANDLER_MAP = new HashMap<>();

    /**
     * 消息处理
     * @param messageMap messageMap
     * @param attachment attachment
     */
    void handle(Map<String, Object> messageMap, DouyuLogin attachment);
}
