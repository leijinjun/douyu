package com.lei2j.douyu.admin.web.config;

import com.lei2j.douyu.admin.web.interceptor.AuthenticationInterceptor;
import com.lei2j.douyu.admin.web.interceptor.HttpRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Value("#{'${cors.domain.list}'.split(',')}")
    private String[] origins;

    @Autowired
    private AuthenticationInterceptor authenticationInterceptor;

    @Autowired
    private HttpRequestInterceptor httpRequestInterceptor;

    public WebConfigurer() {}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(httpRequestInterceptor).order(1).addPathPatterns("/**");
//        registry.addInterceptor(authenticationInterceptor)
//                .order(2)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/admin/auth/user/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins(origins)
                .maxAge(10000);
    }
}
