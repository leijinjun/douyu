package com.lei2j.douyu.web.config;

import com.lei2j.douyu.web.interceptor.AuthenticationInterceptor;
import com.lei2j.douyu.web.interceptor.HttpRequestInterceptor;
import com.lei2j.douyu.web.interceptor.LocaleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private HttpRequestInterceptor httpRequestInterceptor;

    @Autowired
    private LocaleInterceptor localeInterceptor;

    public WebConfigurer() {
        super();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(httpRequestInterceptor);
        registry.addInterceptor(authenticationInterceptor);
        registry.addInterceptor(localeInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedOrigins("*")
                .maxAge(10000);
    }
}
