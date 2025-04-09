package com.cotato.kampus.domain.chat.domain;

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