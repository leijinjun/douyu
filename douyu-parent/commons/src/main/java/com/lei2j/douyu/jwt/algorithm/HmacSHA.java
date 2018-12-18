package com.lei2j.douyu.jwt.algorithm;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA extends Algorithm {

	public HmacSHA(String name,String algorirthm, Key key) {
		super(name,algorirthm,key);
	}
	
	private static byte[] hmacSha(String algorithm,String origin,byte[] key){
        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKey secretKey = new SecretKeySpec(key,algorithm);
            mac.init(secretKey);
            return mac.doFinal(origin.getBytes(Charset.forName("utf-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
	public byte[] sign(String input) {
		Key key = super.getKey();
		return hmacSha(algorithm, input, key.getSecretKey().getBytes(Charset.forName("utf-8")));
	}

	@Override
	public boolean verify(String input,byte[] signature){
		Key key = super.getKey();
		byte[] data = hmacSha(algorithm, input, key.getSecretKey().getBytes(Charset.forName("utf-8")));
		return data != null && Arrays.equals(data, signature);
	}
}
