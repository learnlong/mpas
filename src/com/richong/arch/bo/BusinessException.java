package com.richong.arch.bo;

import com.richong.arch.exception.BaseException;



@SuppressWarnings("serial")
public class BusinessException extends BaseException {
	
	
	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	
}
