package com.lei2j.douyu.util;


import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lei2j on 2018/12/1.
 */
public class EncryptUtil {

    private EncryptUtil(){}

    public static String md5(String input){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(input.getBytes(Charset.forName("utf-8")));
            return HexUtil.toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
