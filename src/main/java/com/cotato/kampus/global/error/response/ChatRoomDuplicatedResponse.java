package com.cotato.kampus.global.error.response;

import com.cotato.kampus.global.error.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;

public record ChatRoomDuplicatedResponse (
	String code,
	String message,
	String method,
	String requestURI,
	Long existingChatRoomId
) {

	public static ChatRoomDuplicatedResponse of(ErrorCode errorCode, HttpServletRequest request, Long existingChatRoomId) {
		return new ChatRoomDuplicatedResponse(
			errorCode.getCode(),
			errorCode.getMessage(),
			request.getMethod(),
			request.getRequestURI(),
			existingChatRoomId
		);
	}
}
