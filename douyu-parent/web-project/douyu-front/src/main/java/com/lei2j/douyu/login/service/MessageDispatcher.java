package com.lei2j.douyu.login.service;

/**
 * 斗鱼消息处理分发
 * @author leijinjun
 *
 */
public interface MessageDispatcher {

	void dispatch(DouyuMessage douyuMessage);
	
}
