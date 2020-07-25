/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.util;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * RSA 非对称加密解密
 * Created by lei2j on 2018/12/4.
 */
public class RSAUtil {

    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

    private static final int KEY_SIZE = 2048;

    private RSAUtil(){}

    /**
     * 生成密钥对
     */
    public static String[] getKeys(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE,new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String[] keys = {Base64Util.encodeToString(publicKey.getEncoded()),
                    Base64Util.encodeToString(privateKey.getEncoded())};
            return keys;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(String privateKey,String input){
        try {
            Objects.requireNonNull(input, "input is null");
            Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey generatePrivate =
                    keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decode(privateKey)));
            rsaCipher.init(Cipher.ENCRYPT_MODE, generatePrivate);
            byte[] origin = input.getBytes(Charset.forName("utf-8"));
            int length = origin.length;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int offset = 0; offset < length; ) {
                int inputLen = 128;
                if ((offset + inputLen) > length) {
                    inputLen = length - offset;
                }
                byte[] b = rsaCipher.doFinal(origin, offset, inputLen);
                offset += inputLen;
                outputStream.write(b);
            }
            byte[] rb = outputStream.toByteArray();
            outputStream.close();
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String publicKey,byte[] input){
        try {
            Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey generatePublic = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Util.decode(publicKey)));
            rsaCipher.init(Cipher.DECRYPT_MODE, generatePublic);
            int length = input.length;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int offset = 0; offset < length; ) {
                int inputLen = 256;
                if ((offset + inputLen) > length) {
                    inputLen = length - offset;
                }
                byte[] b = rsaCipher.doFinal(input, offset, inputLen);
                offset += inputLen;
                outputStream.write(b);
            }
            byte[] rb = outputStream.toByteArray();
            outputStream.close();
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
