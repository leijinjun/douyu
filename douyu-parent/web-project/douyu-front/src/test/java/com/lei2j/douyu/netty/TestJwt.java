package com.lei2j.douyu.netty;

import com.lei2j.douyu.jwt.*;
import com.lei2j.douyu.jwt.algorithm.Algorithm;

import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by lei2j on 2018/12/4.
 */
public class TestJwt {

    @Test
    public void test1(){
        String jwt = JwtBuilder.builder().setIssuedAt(new Date())
                .setAudience("www").addPublicClaim("name","冰与火")
                .setIssuer("lei").build()
                .sign(Algorithm.hmacSHA256("1234"));
        System.out.println(jwt);
    }

    @Test
    public void test2(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoi5Yaw5LiO54GrIiwiaXNzIjoibGVpIiwiYXVkIjoid3d3IiwiaWF0IjoxNTQ0NDYwMDE5MjY4fQ.luYcvrvlXdboUw24qsvqE2biW1mmSdHAq_V2KtXCRwM";
        JwtDecoder jwtDecoder = JwtDecoder.decode(token);
        JwtVerify jwtVerify = new JwtVerify(jwtDecoder, Algorithm.hmacSHA256("12343123123"), new DefaultJwtClaimsValidator());
        System.out.println(jwtVerify.verify());
    }

    @Test
    public void test3(){
        Reference<String> reference  = new SoftReference<>("232");
        System.out.println(reference.get());
        Reference<String> weakReference = new WeakReference<>("weak");
        System.out.println(weakReference.get());
    }
}
