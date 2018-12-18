package com.lei2j.douyu.jwt.algorithm;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.lei2j.douyu.util.Base64Util;

public class SHAWithRSA extends Algorithm {
	
	public SHAWithRSA(String name,String algorithm, Key key) {
        super(name,algorithm,key);
    }

    @Override
	public byte[] sign(String input) {
        Key key = super.getKey();
        return sign(algorithm, key, input);
	}

    @Override
	public boolean verify(String input,byte[] signature){
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            Key key = super.getKey();
            java.security.PublicKey generatePublic = keyFactory.generatePublic(new X509EncodedKeySpec(Base64Util.base64Decode(key.getSecretKey())));
            Signature instance = Signature.getInstance(algorithm);
            instance.initVerify(generatePublic);
            instance.update(input.getBytes(Charset.forName("utf-8")));
            return instance.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        return false;
    }

	private static byte[] sign(String algorithm,Key key,String input){
        try {
            Signature signature = Signature.getInstance(algorithm);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey generatePrivate = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.base64Decode(key.getSecretKey())));
            signature.initSign(generatePrivate);
            signature.update(input.getBytes(Charset.forName("utf-8")));
            return signature.sign();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (InvalidKeySpecException e){
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
}
