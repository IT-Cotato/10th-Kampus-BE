package com.cotato.kampus.domain.chat.domain;

public record ChatMessageSnapshot(
	Long id,
	Long chatroomId,
	Long senderId,
	String content,
	boolean isImage,
	boolean isRead,
	boolean isMine
) {
}