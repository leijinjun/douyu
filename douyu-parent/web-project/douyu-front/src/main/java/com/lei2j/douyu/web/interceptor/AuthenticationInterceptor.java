package com.lei2j.douyu.web.interceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.WebConstants;
import com.lei2j.douyu.jwt.DefaultJwtClaimsValidator;
import com.lei2j.douyu.jwt.JwtDecoder;
import com.lei2j.douyu.jwt.JwtVerify;
import com.lei2j.douyu.jwt.algorithm.Algorithm;
import com.lei2j.douyu.util.CookieUtil;
import com.lei2j.douyu.web.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	private static String JWT_TOKEN_NAME = "one_token";

	private final String SECRET_KEY = "";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String jwtToken = request.getParameter(JWT_TOKEN_NAME);
		if (jwtToken == null) {
			jwtToken = request.getHeader(JWT_TOKEN_NAME);
		}
		if (jwtToken == null) {
			jwtToken = CookieUtil.getValueByCookie(request, JWT_TOKEN_NAME);
		}
		boolean verify = false;
		if (jwtToken != null) {
			JwtDecoder jwtDecoder = JwtDecoder.decode(jwtToken);
			JwtVerify jwtVerify = new JwtVerify(jwtDecoder, Algorithm.hmacSHA256(SECRET_KEY), new DefaultJwtClaimsValidator());
			verify = jwtVerify.verify();
		}
		if (!verify) {
			LOGGER.warn("Request URI:{},OriginIP:{},用户未认证", request.getRequestURI(), request.getAttribute(WebConstants.REQUEST_ATTR_ORIGIN_IP));
			setUnAuthResponse(response);
		}
		return verify;
	}

	private void setUnAuthResponse(HttpServletResponse response) throws IOException{
		ServletOutputStream outputStream = response.getOutputStream();
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		Response unauthenticated = Response.UNAUTHENTICATED;
		String authJsonStr = JSONObject.toJSONString(unauthenticated);
		outputStream.write(authJsonStr.getBytes(Charset.forName("utf-8")));
		outputStream.flush();
		outputStream.close();
	}
}
