package com.lei2j.douyu.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by lei2j on 2018/12/2.
 */
public class Base64Util {

    private Base64Util(){}

    public static byte[] base64Encode(String origin){
        return base64Encode(origin.getBytes(Charset.forName("utf-8")));
    }

    public static byte[] base64Encode(byte[] origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encode(origin);
    }

    public static String base64EncodeToString(byte[] origin){
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(origin);
    }

    public static String base64EncodeToString(String origin){
        return base64EncodeToString(origin.getBytes(Charset.forName("utf-8")));
    }

    public static byte[] base64Decode(String origin){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(origin);
    }

    public static byte[] base64Decode(byte[] origin){
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(origin);
    }

    public static String base64UrlEncode(String origin){
        return base64UrlEncode(origin.getBytes(Charset.forName("utf-8")));
    }

    public static String base64UrlEncode(byte[] origin){
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String base64Str = encoder.encodeToString(origin);
        return replace(base64Str);
    }

    public static byte[] base64UrlDecode(String origin){
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        return urlDecoder.decode(origin);
    }

    public static byte[] base64UrlDecode(byte[] origin){
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        return urlDecoder.decode(origin);
    }

    private static String replace(String str){
        return str.replaceAll("=","");
    }
}
