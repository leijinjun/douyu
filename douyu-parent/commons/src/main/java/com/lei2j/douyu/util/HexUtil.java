package com.lei2j.douyu.util;

/**
 * Created by lei2j on 2018/12/4.
 */
public class HexUtil {

    public static String toHexString(byte[] bs){
        StringBuilder sb = new StringBuilder();
        for (byte b:
                bs) {
            int var1 = 0XFF & b;
            String var2 = Integer.toHexString(var1);
            if (var2.length() == 1) {
                sb.append(0);
            }
            sb.append(var2);
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hexStr) {
        if ((hexStr.length() & 1) == 1) {
            hexStr = "0"+hexStr;
        }
        int len = (hexStr.length() + 1) / 2;
        byte[] bs = new byte[len];
        for (int i = 0,j=0; i < hexStr.length(); i += 2,j++) {
            bs[j] = (byte)Integer.parseInt(hexStr.substring(i,i+2),16);
        }
        return bs;
    }
}
