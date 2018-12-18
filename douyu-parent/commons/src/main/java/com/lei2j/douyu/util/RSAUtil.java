package com.lei2j.douyu.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * RSA 非对称加密解密
 * Created by lei2j on 2018/12/4.
 */
public class RSAUtil {

    private static final String ALGORITHM = "RSA";

    private static final int KEY_SIZE = 2048;

    private static final int MAX_LENGTH = 245;

    private RSAUtil(){}

    /**
     * 生成密钥对
     * @return
     */
    public static String[] getKeys(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE,new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            String[] keys = {Base64Util.base64EncodeToString(publicKey.getEncoded()),Base64Util.base64EncodeToString(privateKey.getEncoded())};
            return keys;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encrypt(String privateKey,String input){
        try {
            Objects.requireNonNull(input,"input is null");
            byte[] origin = input.getBytes(Charset.forName("utf-8"));
            if (origin.length > MAX_LENGTH) {
                throw new IllegalArgumentException("input length is too long,the max input length is "+MAX_LENGTH);
            }
            Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey generatePrivate = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.base64Decode(privateKey)));
            rsaCipher.init(Cipher.ENCRYPT_MODE,generatePrivate);
            byte[] bs = rsaCipher.doFinal(origin);
            return bs;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e){
            e.printStackTrace();
        } catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String publicKey,byte[] origin){
        try {
            Cipher rsaCipher = Cipher.getInstance(ALGORITHM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey generatePublic = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Util.base64Decode(publicKey)));
            rsaCipher.init(Cipher.DECRYPT_MODE,generatePublic);
            return rsaCipher.doFinal(origin);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
