package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessage;

public record ChatNotificationResult(
	ChatMessage chatMessage,
	ChatNotification notification,
	Long receiverId
) {
	public static ChatNotificationResult of(ChatMessage chatMessage, ChatNotification notification, Long receiverId) {
		return new ChatNotificationResult(
			chatMessage,
			notification,
			receiverId
		);
	}
}