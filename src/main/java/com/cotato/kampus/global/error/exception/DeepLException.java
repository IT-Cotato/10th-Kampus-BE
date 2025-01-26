package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

public class DeepLException extends RuntimeException {

	private final ErrorCode errorCode;

	public DeepLException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}