package com.lei2j.douyu.netty;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lei2j on 2018/12/16.
 */
public class TestRegex {

    @Test
    public void test4(){
        Matcher matcher = Pattern.compile("^(.(?!(\\.)\\2))+$").matcher("abc2.3.1$%^^%.&*((64");
        System.out.println(matcher.find());
    }

    @Test
    public void test5(){
        Matcher matcher = Pattern.compile("^\\w((?!(\\.)\\2)[\\w\\.]){0,62}\\w@((?!(\\.)\\4)[\\w\\.]){3,253}$").matcher("d.a.s@p.om");
        System.out.println(matcher.find());
    }

    @Test
    public void test6(){
        BigDecimal a = new BigDecimal("112");
        BigDecimal b = new BigDecimal("-2");
        Long userId = 22312312322L;
        userId = userId*31;
        String accountNo = "1231";
        String md5Hex = DigestUtils.md5Hex(userId + accountNo);
        String aM = a.multiply(BigDecimal.valueOf(10000L)).setScale(0,BigDecimal.ROUND_FLOOR).toPlainString();
        String bM = b.multiply(BigDecimal.valueOf(10000L)).setScale(0,BigDecimal.ROUND_FLOOR).toPlainString();
        System.out.println(bM);
        System.out.println(userId+":"+md5Hex);
        String md5Hex1 = DigestUtils.md5Hex(aM + bM);
        System.out.println(md5Hex1);
        String secret = "tfXHIusTzFQyMsrY7TULFT4OAz5JGk0";
        String md5Hex2 = DigestUtils.md5Hex(md5Hex + md5Hex1 + secret);
        System.out.println(md5Hex2);
    }

    private String generateHash(Long userId,String accountNo,BigDecimal f1,BigDecimal a1){
        String s1 = DigestUtils.md5Hex(userId * 31 + accountNo);
        String aM = f1.multiply(BigDecimal.valueOf(10000L)).setScale(0,BigDecimal.ROUND_FLOOR).toPlainString();
        String bM = a1.multiply(BigDecimal.valueOf(10000L)).setScale(0,BigDecimal.ROUND_FLOOR).toPlainString();
        String s2 = DigestUtils.md5Hex(aM + bM);
        String secret = "tfXHIusTzFQyMsrY7TULFT4OAz5JGk0";
        return DigestUtils.md5Hex(s1 + s2 + secret);
    }
}
