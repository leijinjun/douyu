package com.lei2j.douyu.jwt.algorithm;

import java.nio.charset.Charset;

public class Algorithm {

	protected String name;

	protected String algorithm;

	protected Key key;
	
	protected Algorithm(String name,String algorithm,Key key) {
		this.name = name;
		this.algorithm = algorithm;
		this.key = key;
	}
	
	public String alg() {
		return name;
	}

	public Key getKey(){return key;}
	
	//默认实现
	public byte[] sign(String input) {
		return null;
	}

	//默认实现
	public boolean verify(String input,byte[] signature){
		return false;
	}

	private static HmacSHA hmacSHA(String name,String algorithm,Key secretkey) {
		return new HmacSHA(name,algorithm,secretkey);
	}
	
	private static SHAWithRSA shaWithRSA(String name,String algorithm,Key secretkey) {
		return new SHAWithRSA(name,algorithm,secretkey);
	}

	private static SHAwithECDSA shaWithECDSA(String name,String algorithm,Key secretkey) {
		return new SHAwithECDSA(name,algorithm,secretkey);
	}

	public static HmacSHA hmacSHA256(byte[] secretkey) {
		return hmacSHA("HS256","HmacSHA256", Key.keyCreator(new String(secretkey,Charset.forName("utf-8"))));
	}
	
	public static HmacSHA hmacSHA256(String secretkey) {
		return hmacSHA("HS256","HmacSHA256", Key.keyCreator(secretkey));
	}
	
	public static HmacSHA hmacSHA384(byte[] secretkey) {
		return hmacSHA("HS384","HmacSHA384", Key.keyCreator(new String(secretkey,Charset.forName("utf-8"))));
	}
	
	public static HmacSHA hmacSHA384(String secretkey) {
		return hmacSHA("HS384","HmacSHA384", Key.keyCreator(secretkey));
	}
	
	public static HmacSHA hmacSHA512(byte[] secretkey) {
		return hmacSHA("HS512","HmacSHA512", Key.keyCreator(new String(secretkey,Charset.forName("utf-8"))));
	}
	
	public static HmacSHA hmacSHA512(String secretkey) {
		return hmacSHA("HS512","HmacSHA512", Key.keyCreator(secretkey));
	}
	
	public static SHAWithRSA shaWithRSA256(String key) {
		return shaWithRSA("RS256","SHA256withRSA", Key.keyCreator(key));
	}
	
	public static SHAWithRSA shaWithRSA384(String key) {
		return shaWithRSA("RS384","SHA384withRSA", Key.keyCreator(key));
	}
	
	public static SHAWithRSA shaWithRSA512(String key) {
		return shaWithRSA("RS512","SHA512withRSA", Key.keyCreator(key));
	}
	
	public static SHAwithECDSA shaWithECDSA256(String key) {
		return shaWithECDSA("ES256","SHA256withECDSA", Key.keyCreator(key));
	}
	
	public static SHAwithECDSA shaWithECDSA384(String key) {
		return shaWithECDSA("ES384","SHA384withECDSA", Key.keyCreator(key));
	}
	
	public static SHAwithECDSA shaWithECDSA512(String key) {
		return shaWithECDSA("ES512","SHA512withECDSA", Key.keyCreator(key));
	}
	
}
