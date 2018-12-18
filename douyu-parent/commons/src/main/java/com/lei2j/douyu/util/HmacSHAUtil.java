package com.lei2j.douyu.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 * Created by lei2j on 2018/12/2.
 */
public class HmacSHAUtil {

    private static byte[] hmacSha(String algorithm,String origin,String privateKey){
        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKey secretKey = new SecretKeySpec(privateKey.getBytes(Charset.forName("utf-8")),algorithm);
            mac.init(secretKey);
            return mac.doFinal(origin.getBytes(Charset.forName("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hmacSha256(String origin,String privateKey){
        return hmacSha("HmacSHA256",origin,privateKey);
    }

    public static byte[] hmacSha384(String origin,String privateKey){
        return hmacSha("HmacSHA384",origin,privateKey);
    }

    public static byte[] hmacSha512(String origin,String privateKey){
        return hmacSha("HmacSHA512",origin,privateKey);
    }
}
