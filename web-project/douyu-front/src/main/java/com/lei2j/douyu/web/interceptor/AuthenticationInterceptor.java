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

package com.lei2j.douyu.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.WebConstants;
import com.lei2j.douyu.util.CookieUtil;
import com.lei2j.douyu.web.response.Response;
import com.lei2j.jwt.algorithm.Algorithm;
import com.lei2j.jwt.coder.JwtDecoder;
import com.lei2j.jwt.exception.JwtDecoderException;
import com.lei2j.jwt.validator.DefaultJwtClaimsValidator;
import com.lei2j.jwt.validator.JwtVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	private final static String JWT_TOKEN_NAME = "X-Token";

	private final static String SECRET_KEY = "";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		String jwtToken = request.getParameter(JWT_TOKEN_NAME);
		if (jwtToken == null) {
			jwtToken = request.getHeader(JWT_TOKEN_NAME);
		}
		if (jwtToken == null) {
			jwtToken = CookieUtil.getValueByCookie(request, JWT_TOKEN_NAME);
		}
		boolean verify = false;
		if (jwtToken != null) {
			JwtDecoder jwtDecoder = null;
			try {
				jwtDecoder = JwtDecoder.decode(jwtToken);
			} catch (JwtDecoderException e) {
				e.printStackTrace();
				return false;
			}
			JwtVerify jwtVerify = new JwtVerify(jwtDecoder, Algorithm.hmacSHA256(SECRET_KEY), new DefaultJwtClaimsValidator());
			verify = jwtVerify.verify();
		}
		if (!verify) {
			request.setAttribute(WebConstants.USER_LOGIN_STATUS,Boolean.FALSE);
//			setUnAuthResponse(response);
			return false;
		}
		return true;
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
