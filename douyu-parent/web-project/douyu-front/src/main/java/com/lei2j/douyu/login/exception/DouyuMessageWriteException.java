package com.lei2j.douyu.login.exception;

public class DouyuMessageWriteException extends DouyuException{

	private static final long serialVersionUID = 6895928289413498019L;
	
	public DouyuMessageWriteException(String message) {
		super(message);
	}

	public DouyuMessageWriteException(String message, Throwable cause) {
		super(message, cause);
	}

}
