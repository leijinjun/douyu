package com.lei2j.douyu.jwt.algorithm;

import java.nio.charset.Charset;

public class NoneAlgorithm extends Algorithm {

	protected NoneAlgorithm() {
		super("none","NONE",Key.keyCreator(""));
	}

	@Override
	public byte[] sign(String input) {
		return input.getBytes(Charset.forName("utf-8"));
	}

	@Override
	public boolean verify(String input,byte[] signature){
		return false;
	}
}
