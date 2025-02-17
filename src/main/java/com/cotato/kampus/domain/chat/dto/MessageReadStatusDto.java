package com.cotato.kampus.domain.chat.dto;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;

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