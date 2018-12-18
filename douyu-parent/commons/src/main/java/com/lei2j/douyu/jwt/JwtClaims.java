package com.lei2j.douyu.jwt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lei2j on 2018/12/10.
 */
public class JwtClaims {

    private Map<String, Object> claims = new HashMap<>(7);

    public JwtClaims() {}

    public void put(String key, Object value) {
        claims.put(key, value);
    }

    public void putAll(Map<String, Object> extra) {
        claims.putAll(extra);
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
