package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

	private ErrorCode errorCode;

	public JwtException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
