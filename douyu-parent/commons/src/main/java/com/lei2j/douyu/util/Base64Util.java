package com.lei2j.douyu.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by lei2j on 2018/12/2.
 */
public class Base64Util {

    private Base64Util(){}

    public static byte[] encode(String origin){
        return encode(origin.getBytes(Charset.forName("utf-8")));
    }

    public static byte[] encode(byte[] origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(origin);
    }

    public static String encodeToString(byte[] origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(origin);
    }

    public static String encodeToString(String origin){
        return encodeToString(origin.getBytes(Charset.forName("utf-8")));
    }

    public static byte[] decode(String origin){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(origin);
    }

    public static byte[] decode(byte[] origin){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(origin);
    }

    public static String encodeUrl(String origin){
        return encodeUrl(origin.getBytes(Charset.forName("utf-8")));
    }

    public static String encodeUrl(byte[] origin){
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String base64Str = encoder.encodeToString(origin);
        return replace(base64Str);
    }

    public static byte[] decodeUrl(String origin){
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        return urlDecoder.decode(origin);
    }

    public static byte[] decodeUrl(byte[] origin){
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        return urlDecoder.decode(origin);
    }

    private static String replace(String str){
        return str.replaceAll("=","");
    }
}
