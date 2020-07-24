package com.lei2j.douyu.admin.danmu.service;

import java.util.Map;

/**
 * 斗鱼消息处理分发
 * @author leijinjun
 *
 */
public interface MessageDispatcher {

	void dispatch(Map<String, Object> dataMap);
	
}
