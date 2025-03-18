package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class UnivCertException extends RuntimeException {

	private final ErrorCode errorCode;

	public UnivCertException(ErrorCode errorCode, String univCertMessage) {
		super(univCertMessage);
		this.errorCode = errorCode;
	}
}
