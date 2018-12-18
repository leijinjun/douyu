package com.lei2j.douyu.jwt;

/**
 * Created by lei2j on 2018/12/10.
 */
public class JwtHeader {

    /**
     * JWT协议
     */
    private String typ;

    /**
     * 加密算法简称
     */
    private String alg;

    public JwtHeader() {
    }

    public JwtHeader(String typ, String alg) {
        this.typ = typ;
        this.alg = alg;
    }

    public String getTyp() {
        return typ;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
}
