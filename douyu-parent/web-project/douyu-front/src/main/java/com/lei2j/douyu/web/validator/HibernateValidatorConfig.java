package com.lei2j.douyu.web.validator;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * hibernate validator 校验配置
 * Created by lei2j on 2018/12/18.
 */
@Configuration
public class HibernateValidatorConfig {

    @Bean("validator")
    public Validator configValidator(){
        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
        return validator;
    }
}
