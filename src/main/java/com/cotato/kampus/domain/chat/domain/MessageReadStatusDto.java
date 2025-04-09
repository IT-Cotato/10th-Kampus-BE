package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.chat.dao.entity.MessageReadStatusEntity;

public record MessageReadStatusDto(
	Long chatroomId,
	Long userId,
	Long lastReadMessageId
) {
	public static MessageReadStatusDto from(MessageReadStatusEntity entity) {
		return new MessageReadStatusDto(
			entity.getChatroomId(),
			entity.getUserId(),
			entity.getLastReadMessageId()
		);
	}
}