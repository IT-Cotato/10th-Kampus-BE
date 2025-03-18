package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

	private final ErrorCode errorCode;

	public AppException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}