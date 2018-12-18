package com.lei2j.douyu.jwt;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.util.Base64Util;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lei2j on 2018/12/10.
 */
public class JwtDecoder {

    private JwtHeader jwtHeader;

    private JwtClaims claims;

    private String payload;

    private String signature;

    private JwtDecoder(JwtHeader jwtHeader, JwtClaims claims,String payload, String signature) {
        this.jwtHeader = jwtHeader;
        this.claims = claims;
        this.payload = payload;
        this.signature = signature;
    }

    public static JwtDecoder decode(String token) {
        Objects.requireNonNull(token,"token is null");
        String[] sp = token.split("\\.");
        if (sp.length != 3) {
            throw new IllegalArgumentException("token is incorrect");
        }
        String header = sp[0];
        String payload = sp[1];
        String headerJson = new String(Base64Util.base64UrlDecode(header));
        String payloadJson = new String(Base64Util.base64UrlDecode(payload));
        JwtHeader jwtHeader = JSONObject.parseObject(headerJson, JwtHeader.class);
        String alg = jwtHeader.getAlg();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.putAll(JSONObject.parseObject(payloadJson));
        return new JwtDecoder(jwtHeader,jwtClaims,String.format("%s.%s",sp[0],sp[1]),sp[2]);
    }

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
    public Map<String,Object> getPublicClaims() {
        return this.getPublicClaims();
    }

    public JwtHeader getJwtHeader(){
        return this.jwtHeader;
    }

    public JwtClaims getClaims(){
        return this.claims;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getPayload() {
        return payload;
    }
}
