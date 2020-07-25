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

import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSAWithSHA 签名和验证
 * Created by lei2j on 2018/12/2.
 */
public class RSASHAUtil {

    private RSASHAUtil(){}

    /**
     * 生成密钥对
     */
    public static String[] getKeys(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024,new SecureRandom());
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

    public static byte[] signForSHA256(String privateKey,String input){
        return sign("SHA256withRSA",privateKey,input);
    }

    public static byte[] signForSHA384(String privateKey,String input){
        return sign("SHA384withRSA",privateKey,input);
    }

    public static byte[] signForSHA512(String privateKey,String input){
        return sign("SHA512withRSA",privateKey,input);
    }

    public static boolean verifySHA256(String publicKey,String data,byte[] signature){
        return verify("SHA256withRSA",publicKey,data,signature);
    }

    public static boolean verifySHA384(String publicKey,String data,byte[] signature){
        return verify("SHA384withRSA",publicKey,data,signature);
    }

    public static boolean verifySHA512(String publicKey,String data,byte[] signature){
        return verify("SHA512withRSA",publicKey,data,signature);
    }

    private static byte[] sign(String algorithm,String privateKey,String input){
        try {
            Signature signature = Signature.getInstance(algorithm);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey generatePrivate =
                    keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decode(privateKey)));
            signature.initSign(generatePrivate);
            signature.update(input.getBytes(Charset.forName("utf-8")));
            return signature.sign();
        }catch (NoSuchAlgorithmException|InvalidKeySpecException|InvalidKeyException|SignatureException e){
            e.printStackTrace();
        }
        return null;
    }

    private static boolean verify(String algorithm,String publicKey,String data,byte[] signature){
        try {
            Signature instance = Signature.getInstance(algorithm);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey generatePublic = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Util.decode(publicKey)));
            instance.initVerify(generatePublic);
            instance.update(data.getBytes(Charset.forName("utf-8")));
            return instance.verify(signature);
        } catch (NoSuchAlgorithmException|InvalidKeySpecException|InvalidKeyException|SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
}
