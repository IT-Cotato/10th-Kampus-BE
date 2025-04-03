package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;

public record ChatNotificationResult(
	ChatMessageEntity chatMessageEntity,
	ChatNotification notification,
	Long receiverId
) {
	public static ChatNotificationResult of(ChatMessageEntity chatMessageEntity, ChatNotification notification, Long receiverId) {
		return new ChatNotificationResult(
			chatMessageEntity,
			notification,
			receiverId
		);
	}
}