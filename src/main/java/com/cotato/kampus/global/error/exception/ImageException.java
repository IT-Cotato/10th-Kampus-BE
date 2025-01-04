package com.cotato.kampus.global.error.exception;

import java.io.IOException;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ImageException extends IOException {

	private ErrorCode errorCode;
}
