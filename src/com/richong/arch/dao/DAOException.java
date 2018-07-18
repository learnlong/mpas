package com.richong.arch.dao;

import com.richong.arch.exception.BaseException;

@SuppressWarnings("serial")
public class DAOException extends BaseException {
	public DAOException() {
		super();
	}

	public DAOException(String message) {
		super(message);
	}

	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
}
