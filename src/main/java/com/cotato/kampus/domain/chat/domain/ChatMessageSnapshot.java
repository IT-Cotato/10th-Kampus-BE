package com.cotato.kampus.domain.chat.domain;

public record ChatMessageSnapshot(
	Long id,
	Long chatroomId,
	Long senderId,
	String content,
	boolean isRead,
	boolean isMine
) {
}