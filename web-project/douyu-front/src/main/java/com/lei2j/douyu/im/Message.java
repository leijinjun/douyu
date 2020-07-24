package com.lei2j.douyu.im;

/**
 * @author lei2j
 */
public interface Message {

    /**
     * 消息结束标志
     */
	String END_FLAG = "\0";

    String getBody();
    
    ChatType getType();
}
