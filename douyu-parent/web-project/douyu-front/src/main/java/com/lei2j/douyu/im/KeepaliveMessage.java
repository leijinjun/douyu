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
    
    private static final ChatType CHAT_TYPE = ChatType.KEEPLIVE;

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
