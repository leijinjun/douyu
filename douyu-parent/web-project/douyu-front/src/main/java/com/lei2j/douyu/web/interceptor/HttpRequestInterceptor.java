package com.lei2j.douyu.web.interceptor;


import com.lei2j.douyu.core.constant.WebConstants;
import com.lei2j.douyu.util.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpRequestInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Long st = System.currentTimeMillis();
		String uri = request.getRequestURI();
		String originIP = IPUtil.getOriginRequestIP(request);
		LOGGER.info("Request URI:{}",uri);
		request.setAttribute(WebConstants.REQUEST_ATTR_ORIGIN_IP,originIP);
		request.setAttribute("st", st);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView){
		long ed = System.currentTimeMillis();
		Long st = (Long) request.getAttribute("st");
		LOGGER.info("Response time:{}ms",ed-st);
	}

}
