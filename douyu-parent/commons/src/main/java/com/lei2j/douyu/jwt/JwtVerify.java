package com.lei2j.douyu.jwt;

import com.lei2j.douyu.jwt.algorithm.Algorithm;
import com.lei2j.douyu.util.Base64Util;

/**
 * Created by lei2j on 2018/12/10.
 */
public class JwtVerify {

    private JwtDecoder jwtDecoder;

    private Algorithm algorithm;

    private JwtClaimsValidator claimsValidator;

    public JwtVerify(JwtDecoder jwtDecoder, Algorithm algorithm, JwtClaimsValidator claimsValidator) {
        this.jwtDecoder = jwtDecoder;
        this.algorithm = algorithm;
        this.claimsValidator = claimsValidator;
    }

    public boolean verify(){
        String signature = jwtDecoder.getSignature();
        String payload = jwtDecoder.getPayload();
        byte[] bs = Base64Util.base64UrlDecode(signature);
        return claimsValidator.validate(jwtDecoder)&& algorithm.verify(payload, bs);
    }
}
