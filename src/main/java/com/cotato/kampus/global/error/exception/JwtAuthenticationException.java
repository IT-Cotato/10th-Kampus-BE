package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

public class JwtAuthenticationException extends AppException {
	public JwtAuthenticationException(ErrorCode errorCode) {
		super(errorCode);
	}
}