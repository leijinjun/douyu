package com.lei2j.douyu.util;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * ECDSA 非对称加密 签名和验证
 * @author leijinjun
 *
 */
public class ECDSAUtil {
	
	private ECDSAUtil() {}

	public static String[] getKeys() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
			keyPairGenerator.initialize(256, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			byte[] privateKey = keyPair.getPrivate().getEncoded();
			byte[] publicKey = keyPair.getPublic().getEncoded();
			return new String[]{Base64Util.encodeToString(publicKey),Base64Util.encodeToString(privateKey)};
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] signForSHA256(String privateKey,String input){
		return sign("SHA256withECDSA",privateKey,input);
	}

	public static byte[] signForSHA384(String privateKey,String input){
		return sign("SHA384withECDSA",privateKey,input);
	}

	public static byte[] signForSHA512(String privateKey,String input){
		return sign("SHA512withECDSA",privateKey,input);
	}

	public static boolean verifyForSHA256(String publicKey,String data,byte[] signature){
		return verify("SHA256withECDSA",publicKey,data,signature);
	}

	public static boolean verifyForSHA384(String publicKey,String data,byte[] signature){
		return verify("SHA384withECDSA",publicKey,data,signature);
	}

	public static boolean verifyForSHA512(String publicKey,String data,byte[] signature){
		return verify("SHA512withECDSA",publicKey,data,signature);
	}
	
	private static byte[] sign(String algorithm,String privateKey,String input) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			PrivateKey generatePrivate =
					keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decode(privateKey)));
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(generatePrivate);
			byte[] bs = input.getBytes(Charset.forName("utf-8"));
			signature.update(bs);
			return signature.sign();
		} catch (NoSuchAlgorithmException|InvalidKeySpecException|InvalidKeyException|SignatureException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean verify(String algorithm,String publicKey,String data,byte[] signature) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("EC");
			PublicKey generatePublic = keyFactory
					.generatePublic(new X509EncodedKeySpec(Base64Util.decode(publicKey)));
			Signature instance = Signature.getInstance(algorithm);
			instance.initVerify(generatePublic);
			instance.update(data.getBytes(Charset.forName("utf-8")));
			return instance.verify(signature);
		} catch (NoSuchAlgorithmException|InvalidKeySpecException|InvalidKeyException|SignatureException e) {
			e.printStackTrace();
		}
		return false;
	}
}
