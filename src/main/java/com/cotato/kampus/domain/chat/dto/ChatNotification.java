package com.cotato.kampus.domain.chat.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ChatNotification(
	Long chatroomId,
	String lastChatMessage,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime lastChatTime,
	Long messageId,
	Long unreadCount
) {
	public static ChatNotification from(ChatMessage message, Long unreadCount) {
		return new ChatNotification(
			message.getChatroomId(),
			message.getContent(),
			message.getCreatedTime(),
			message.getId(),
			unreadCount
		);
	}
}