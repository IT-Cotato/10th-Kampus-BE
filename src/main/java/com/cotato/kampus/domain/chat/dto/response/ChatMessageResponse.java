package com.cotato.kampus.domain.chat.dto.response;

import com.cotato.kampus.domain.chat.domain.ChatMessage;

public record ChatMessageResponse(
	Long chatRoomId,
	Long senderId,
	String content,
	boolean isRead,
	boolean isMine
) {
	public static ChatMessageResponse from(ChatMessage chatMessage, Long currentUserId) {
		return new ChatMessageResponse(
			chatMessage.getChatroomId(),
			chatMessage.getSenderId(),
			chatMessage.getContent(),
			chatMessage.getIsRead(),
			chatMessage.getSenderId().equals(currentUserId)
		);
	}
}