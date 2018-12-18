package com.lei2j.douyu.jwt;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.jwt.algorithm.Algorithm;
import com.lei2j.douyu.util.Base64Util;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lei2j on 2018/12/1.
 */
public class JwtBuilder {

    private Jwt jwt;

    private JwtBuilder(){
        this.jwt = new Jwt();
    }

    public static JwtBuilder builder(){
        return new JwtBuilder();
    }

    /**
     * 设置签发主体
     *
     * @param iss
     * @return
     */
    public JwtBuilder setIssuer(String iss) {
        Objects.requireNonNull(iss, "iss is null");
        jwt.claims.put(ReservedClaims.ISSUER, iss);
        return this;
    }

    /**
     * 设置所有人
     *
     * @param sub
     * @return
     */
    public JwtBuilder setSubject(String sub) {
        Objects.requireNonNull(sub, "sub is null");
        jwt.claims.put(ReservedClaims.SUBJECT, sub);
        return this;
    }

    /**
     * 设置接收对象
     *
     * @param aud
     * @return
     */
    public JwtBuilder setAudience(String aud) {
        Objects.requireNonNull(aud, "aud is null");
        jwt.claims.put(ReservedClaims.AUDIENCE, aud);
        return this;
    }

    /**
     * 设置过期时间
     *
     * @param exp
     * @return
     */
    public JwtBuilder setExpirationTime(Long exp) {
        Objects.requireNonNull(exp, "exp is null");
        jwt.claims.put(ReservedClaims.EXPIRATION_TIME, exp);
        return this;
    }

    /**
     * 设置过期时间
     *
     * @param exp
     * @return
     */
    public JwtBuilder setExpirationTime(Date exp) {
        Objects.requireNonNull(exp, "exp is null");
        this.setExpirationTime(exp.getTime());
        return this;
    }

    /**
     * 设置生效时间
     *
     * @param nbf
     * @return
     */
    public JwtBuilder setnotBefore(Long nbf) {
        Objects.requireNonNull(nbf, "nbf is null");
        jwt.claims.put(ReservedClaims.NOT_BEFORE, nbf);
        return this;
    }

    /**
     * 设置生效时间
     *
     * @param nbf
     * @return
     */
    public JwtBuilder setnotBefore(Date nbf) {
        Objects.requireNonNull(nbf, "nbf is null");
        this.setnotBefore(nbf.getTime());
        return this;
    }

    /**
     * 设置签发时间
     *
     * @param iat
     * @return
     */
    public JwtBuilder setIssuedAt(Long iat) {
        Objects.requireNonNull(iat, "iat is null");
        jwt.claims.put(ReservedClaims.ISSUED_AT, iat);
        return this;
    }

    /**
     * 设置签发时间
     *
     * @param iat
     * @return
     */
    public JwtBuilder setIssuedAt(Date iat) {
        Objects.requireNonNull(iat, "iat is null");
        this.setIssuedAt(iat.getTime());
        return this;
    }

    /**
     * JWT唯一签名
     *
     * @param jti
     * @return
     */
    public JwtBuilder setJwtID(String jti) {
        Objects.requireNonNull(jti, "jti is null");
        jwt.claims.put(ReservedClaims.JWT_ID, jti);
        return this;
    }

    /**
     * 设置额外的claims
     *
     * @param extra
     * @return
     */
    public JwtBuilder addAllPublicClaims(Map<String, Object> extra) {
        jwt.claims.putAll(extra);
        return this;
    }

    public JwtBuilder addPublicClaim(String key,Object value){
        jwt.claims.put(key,value);
        return this;
    }

    public Jwt build(){
        return jwt;
    }

    private class Payload {

    }

    public class Jwt {

        private JwtHeader jwtHeader = new JwtHeader("JWT","NONE");

        private JwtClaims claims = new JwtClaims();

        private Jwt(){}

        /**
         * return jwt header
         *
         * @return
         */
        public String getType(){
            return jwtHeader.getTyp();
        }

        /**
         * return jwt algorithm
         *
         * @return
         */
        public String getAlgorithm(){
            return jwtHeader.getAlg();
        }

        /**
         * return jwt issuer
         *
         * @return
         */
        public String getIssuer() {
            Object iss = claims.getClaims().get(ReservedClaims.ISSUER);
            return iss == null ? null : String.valueOf(iss);
        }

        /**
         * return jwt subject
         *
         * @return
         */
        public String getSubject() {
            Object sub = claims.getClaims().get(ReservedClaims.SUBJECT);
            return sub == null ? null : String.valueOf(sub);
        }

        /**
         * return jwt Audience
         *
         * @return
         */
        public String getAudience() {
            Object aud = claims.getClaims().get(ReservedClaims.AUDIENCE);
            return aud == null ? null : String.valueOf(aud);
        }

        /**
         * return jwt expiration time
         *
         * @return
         */
        public Date getExpirationTime() {
            Long exp = (Long) claims.getClaims().get(ReservedClaims.EXPIRATION_TIME);
            if (exp != null) {
                return new Date(exp);
            }
            return null;
        }

        /**
         * return jwt not before time
         *
         * @return
         */
        public Date getNotBefore() {
            Long exp = (Long) claims.getClaims().get(ReservedClaims.NOT_BEFORE);
            if (exp != null) {
                return new Date(exp);
            }
            return null;
        }

        /**
         * return jwt issued at
         *
         * @return
         */
        public Date getIssuedAt() {
            Long exp = (Long) claims.getClaims().get(ReservedClaims.ISSUED_AT);
            if (exp != null) {
                return new Date(exp);
            }
            return null;
        }

        /**
         * return jwt ID
         *
         * @return
         */
        public String getJwtID() {
            Object jwtId = claims.getClaims().get(ReservedClaims.JWT_ID);
            return jwtId == null ? null : String.valueOf(jwtId);
        }

        /**
         * return public claims
         *
         * @return
         */
        public Map<String,Object> getClaims() {
            return this.claims.getClaims();
        }

        /**
         * header base64 encode
         *
         * @return
         */
        private String toBase64UrlEncodeHeader() {
            return Base64Util.base64UrlEncode(JSONObject.toJSONString(jwtHeader));
        }

        private String toBase64UrlEncodePayload() {
            return Base64Util.base64UrlEncode(JSONObject.toJSONString(claims.getClaims()));
        }

        public String sign(Algorithm algorithm){
        	jwtHeader.setAlg(algorithm.alg());
            String data = String.format("%s.%s",toBase64UrlEncodeHeader(),toBase64UrlEncodePayload());
            return String.format("%s.%s",data,Base64Util.base64UrlEncode(algorithm.sign(data)));
        }
    }
}
