package com.lei2j.douyu.login.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public abstract class AbstractMessageHandler implements MessageHandler{

    protected Logger logger  = LoggerFactory.getLogger(this.getClass());
    
    protected AbstractMessageHandler(){
        this.afterSetHandler();
    }

    /**
     * 设置handler类
     */
    protected abstract void afterSetHandler();
}
