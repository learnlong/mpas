package com.richong.arch.exception;


public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -7639442930225696611L;

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public BaseException(Throwable cause) {
		super(cause);
	}	
	
	/*
	public static final String getString(String key) {
		return (String) getObject(key);
	}

	public static final String[] getStringArray(String key) {
		return (String[]) getObject(key);
	}

	
	public static final Object getObject(String key) {
		try {
			return labels.getObject(key);
		}
		catch (Exception e) {
		}
		return key;
	}*/

}
