package com.lei2j.douyu.web.config;

import com.lei2j.douyu.web.interceptor.AuthenticationInterceptor;
import com.lei2j.douyu.web.interceptor.HttpRequestInterceptor;
import com.lei2j.douyu.web.interceptor.LocaleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
}
