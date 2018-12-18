package com.lei2j.douyu.util;

import java.util.Random;

/**
 * @author lei2j
 */
public class RandomUtil {

	private RandomUtil() {}

    private static final char[] SER = {'0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public static int getInt(int max){
        Random random = new Random();
        return random.nextInt(max);
    }

    public static String getString(int len){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int max = SER.length;
        for (int i = 0; i < len; i++) {
            sb.append(SER[random.nextInt(max)]);
        }
        return sb.toString();
    }
}
