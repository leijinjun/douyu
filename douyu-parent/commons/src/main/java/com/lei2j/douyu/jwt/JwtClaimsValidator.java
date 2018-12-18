package com.lei2j.douyu.jwt;

/**
 * Created by lei2j on 2018/12/11.
 */
public interface JwtClaimsValidator {

    boolean validate(JwtDecoder jwtDecoder);
}
