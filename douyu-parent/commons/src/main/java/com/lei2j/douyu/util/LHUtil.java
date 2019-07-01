package com.lei2j.douyu.util;

/**
 * @author lei2j
 * Number lower and Height transform
 * Created by lei2j on 2018/5/26.
 */
public class LHUtil {

    /**
     * 整数转换为小端字节数
     */
    public static byte[] toLowerInt(int i){
        byte b1 = (byte)i;
        byte b2 = (byte)(i>>8);
        byte b3 = (byte)(i>>16);
        byte b4 = (byte)(i>>24);
        return new byte[]{b1,b2,b3,b4};
    }

    /**
     * 整数转换为大端字节数
     */
    public static byte[] toHeightInt(int i){
        byte b1 = (byte)(i>>24);
        byte b2 = (byte)(i>>16);
        byte b3 = (byte)(i>>8);
        byte b4 = (byte)i;
        return new byte[]{b1,b2,b3,b4};
    }

    /**
     * 整数转换为小端字节数
     */
    public static byte[] toLowerShort(short i){
        byte b1 = (byte)i;
        byte b2 = (byte)(i>>8);
        return new byte[]{b1,b2};
    }

    /**
     * 整数转换为大端字节数
     */
    public static byte[] toHeightShort(short i){
        byte b2 = (byte)i;
        byte b1 = (byte)(i>>8);
        return new byte[]{b1,b2};
    }

    /**
     * 字节数转换为小端整数
     */
    public static int lowerToInt(byte[] b){
        return lowerToInt(b,0);
    }

    public static int lowerToInt(byte[] b,int srcPos){
        if(b==null){
            throw new NullPointerException("Argument b is null");
        }
        if (srcPos < 0||srcPos>b.length-1) {
            throw new IllegalArgumentException("invalid srcPos:" + srcPos);
        }
        int i1 = 0xff&b[srcPos];
        int i2 = (0xff&b[++srcPos])<<8;
        int i3 = (0xff&b[++srcPos])<<16;
        int i4 = (0xff&b[++srcPos])<<24;
        return i1|i2|i3|i4;
    }

    /**
     * 字节数转换为小端整数
     */
    public static short lowerToShort(byte[] b){
        return lowerToShort(b,0);
    }

    public static short lowerToShort(byte[] b,int srcPos){
        if(b==null){
            throw new NullPointerException("Argument b is null");
        }
        if (srcPos < 0||srcPos>b.length-1) {
            throw new IllegalArgumentException("invalid srcPos:" + srcPos);
        }
        int i1 = b[srcPos]&0xff;
        int i2 = (b[++srcPos]&0xff)<<8;
        return (short)(i1|i2);
    }

    /*public static int lowerToInt1(byte[] b){
        ByteArrayInputStream inputStream = null;
        int len = b.length;
        byte[] newBytes = new byte[len];
        for (int i = 0; i < len; i++) {
            newBytes[len-i-1] = b[i];
        }
        try {
            inputStream = new ByteArrayInputStream(newBytes);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            return dataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }*/
}
