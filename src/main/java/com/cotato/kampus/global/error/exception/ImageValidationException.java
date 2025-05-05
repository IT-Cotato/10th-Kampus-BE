package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class ImageValidationException extends AppException {

	private final String detailMessage;

	public ImageValidationException(ErrorCode errorCode, String detailMessage) {
		super(errorCode);
		this.detailMessage = detailMessage;
	}
}
