package com.cotato.kampus.domain.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ChatRoomPreview(
	Long chatroomId,
	String postTitle,
	String lastChatMessage,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastChatTime
) {
	public static ChatRoomPreview of(Long chatroomId, String title, String lastMessage, LocalDateTime lastMessageTime) {
		return new ChatRoomPreview(chatroomId, title, lastMessage, lastMessageTime);
	}
}