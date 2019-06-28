package com.lei2j.douyu.web.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Created by leijinjun on 2018/12/25.
 */
@Configuration
public class ApplicationBeanConfig {

    /**
     * 设置Validator
     * @return Validator
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
    public RestTemplateBuilder configRestTemplateBuilder(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.setConnectTimeout(10000);
        restTemplateBuilder.setReadTimeout(60000);
        return restTemplateBuilder;
    }

    /*@Bean
    public CacheManagerCustomizer<CaffeineCacheManager> cacheManagerCustomizer() {
        return (cacheManager)-> cacheManager.setAllowNullValues(false);
    }*/

}
