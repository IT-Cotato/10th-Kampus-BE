package com.cotato.kampus.domain.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ChatRoomPreview(
	Long chatroomId,
	String postTitle,
	Long lastMessageId,
	String lastMessageContent,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastChatTime,
	Long unreadCount
) {
	public static ChatRoomPreview of(
		Long chatroomId,
		String title,
		Long lastMessageId,
		String lastMessageContent,
		LocalDateTime lastMessageTime,
		Long unreadCount
	) {
		return new ChatRoomPreview(
			chatroomId, title, lastMessageId, lastMessageContent, lastMessageTime, unreadCount
		);
	}
}