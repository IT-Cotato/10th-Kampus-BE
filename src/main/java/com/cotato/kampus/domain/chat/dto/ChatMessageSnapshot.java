package com.cotato.kampus.domain.chat.dto;

public record ChatMessageSnapshot(
	Long id,
	Long chatroomId,
	Long senderId,
	String content,
	boolean isRead,
	boolean isMine
) {
}