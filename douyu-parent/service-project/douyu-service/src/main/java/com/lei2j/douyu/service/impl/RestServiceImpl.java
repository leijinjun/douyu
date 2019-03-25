package com.lei2j.douyu.service.impl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

/**
 * Created by leijinjun on 2019/2/22.
 */
public class RestServiceImpl extends BaseServiceImpl {

    private final RestTemplate restTemplate;

    public RestServiceImpl(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }
}
