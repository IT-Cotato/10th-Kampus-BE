package com.cotato.kampus.global.error.exception;

import com.cotato.kampus.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class ChatRoomDuplicatedException extends RuntimeException {

	private final ErrorCode errorCode;
	private final Long existingChatRoomId;

	public ChatRoomDuplicatedException(ErrorCode errorCode, Long existingChatRoomId) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.existingChatRoomId = existingChatRoomId;
	}
}