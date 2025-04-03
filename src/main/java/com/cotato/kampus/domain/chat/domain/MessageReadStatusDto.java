package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.chat.dao.entity.MessageReadStatus;

public record MessageReadStatusDto(
	Long chatroomId,
	Long userId,
	Long lastReadMessageId
) {
	public static MessageReadStatusDto from(MessageReadStatus entity) {
		return new MessageReadStatusDto(
			entity.getChatroomId(),
			entity.getUserId(),
			entity.getLastReadMessageId()
		);
	}
}