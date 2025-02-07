package com.cotato.kampus.domain.chat.dto.request;

public record ChatMessageRequest(
	Long chatroomId,
	Long senderId,
	String message
) {
}