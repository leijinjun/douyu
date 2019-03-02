package com.lei2j.douyu.admin.web.config;

import com.lei2j.douyu.web.interceptor.AuthenticationInterceptor;
import com.lei2j.douyu.web.interceptor.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

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
        CorsRegistration corsRegistration = registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("http://127.0.0.1:8883","http://localhost:8883", "https://www.opendanmu.com",
                        "http://www.opendanmu.com" ,"https://www.lei2j.com","http://www.lei2j.com")
                .maxAge(10000);
    }
}
