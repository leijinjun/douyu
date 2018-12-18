package com.lei2j.douyu.jwt;

/**
 * Created by lei2j on 2018/12/11.
 */
public class DefaultJwtClaimsValidator implements JwtClaimsValidator {

    public DefaultJwtClaimsValidator(){}

    @Override
    public boolean validate(JwtDecoder jwtDecoder) {
        return true;
    }
}
