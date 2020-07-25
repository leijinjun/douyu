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

package com.lei2j.douyu.im;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lei2j
 */
public class KeepaliveMessage implements Message{

    /**
     * 当前unix时间戳
     */
    private long tick;
    
    private static final ChatType CHAT_TYPE = ChatType.KEEPALIVE;

    public KeepaliveMessage() {
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

	@Override
	public String getBody() {
		return JSONObject.toJSONString(this).concat(END_FLAG);
	}

	@Override
	public ChatType getType() {
		return CHAT_TYPE;
	}
}
