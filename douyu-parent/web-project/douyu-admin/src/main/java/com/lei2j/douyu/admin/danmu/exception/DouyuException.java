package com.lei2j.douyu.admin.danmu.exception;

public class DouyuException extends RuntimeException{

	private static final long serialVersionUID = 6310350082052887913L;

	public DouyuException(String message) {
		super(message);
	}
	
	public DouyuException(String message,Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public synchronized Throwable getCause() {
		return super.getCause();
	}
}
