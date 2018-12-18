package com.lei2j.douyu.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by lei2j on 2018/6/24.
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected HttpServletRequest request;
    
    protected HttpServletResponse response;

    protected BaseController() {
    }

    @ModelAttribute//在执行方法之前执行此方法
    protected void requestAndResponse(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
        String origin = request.getHeader("Origin");
        if(StringUtils.isNotBlank(origin)){
            logger.info("origin:{}",origin);
            response.setHeader("Access-Control-Allow-Origin",origin);
            response.setHeader("Access-Control-Allow-Credentials","true");
            //Access-Control-Expose-Headers，是否允许客户端访问自定义header字段
            response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE");//复杂CORS请求时的预检请求
            response.setHeader("Access-Control-Max-Age","10");//预检请求时的有效期
        }
    }
}
