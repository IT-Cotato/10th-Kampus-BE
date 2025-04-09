package com.cotato.kampus.domain.chat.api.request;

public record ChatMessageRequest(
	Long chatroomId,
	Long senderId,
	String message
) {
}