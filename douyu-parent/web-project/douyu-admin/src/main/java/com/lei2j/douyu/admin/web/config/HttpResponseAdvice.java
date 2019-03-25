package com.lei2j.douyu.admin.web.config;

import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class HttpResponseAdvice implements ResponseBodyAdvice<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseAdvice.class);

	@Resource
	private MessageSource messageSource;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		if (returnType.getParameterType() == Response.class) {
			return true;
		}
		return false;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		List<Locale> localeList = request.getHeaders().getAcceptLanguageAsLocales();
		Locale locale = Locale.getDefault();
		if (localeList != null && !localeList.isEmpty()) {
			locale = localeList.get(0);
		}
		Response responseBody = (Response) body;
		if (responseBody.getErrCode() != ResponseCode.OK.getCode() && responseBody.getErrMsg() == null) {
			responseBody.text(messageSource.getMessage(String.valueOf(responseBody.getErrCode()), null, locale));
		}
		LOGGER.info("Response message:{}", responseBody);
		return responseBody;
	}

}
