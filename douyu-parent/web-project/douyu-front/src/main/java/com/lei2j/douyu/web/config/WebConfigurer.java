package com.lei2j.douyu.web.config;

import com.lei2j.douyu.web.interceptor.AuthenticationInterceptor;
import com.lei2j.douyu.web.interceptor.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private HttpRequestInterceptor httpRequestInterceptor;

    public WebConfigurer() {}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(httpRequestInterceptor).order(1).addPathPatterns("/**");
        registry.addInterceptor(authenticationInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/user/**");
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
