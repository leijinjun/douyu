package com.lei2j.douyu.jwt.algorithm;

public class Key {

	private String secretKey;

	private Key(String secretKey) {
		this.secretKey = secretKey;
	}

	public static Key keyCreator(String key){
		return new Key(key);
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}