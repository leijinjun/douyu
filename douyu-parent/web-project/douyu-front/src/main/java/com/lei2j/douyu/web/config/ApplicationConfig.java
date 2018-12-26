package com.lei2j.douyu.web.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Created by leijinjun on 2018/12/25.
 */
@Configuration
public class ApplicationConfig {

    /**
     * 设置Validator
     * @return
     */
    @Bean("validator")
    public Validator configValidator(){
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
        return validator;
    }

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer(){
        return (cacheManager)->{
            //do nothing
        };
    }

}
