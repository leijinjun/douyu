package com.lei2j.douyu.danmu.service;

import com.lei2j.douyu.danmu.pojo.DouyuMessage;

/**
 * 斗鱼消息处理分发
 * @author leijinjun
 *
 */
public interface MessageDispatcher {

	void dispatch(DouyuMessage douyuMessage);
	
}
