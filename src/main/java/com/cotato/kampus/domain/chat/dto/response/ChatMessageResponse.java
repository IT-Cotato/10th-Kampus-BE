package com.cotato.kampus.domain.chat.dto.response;

import com.cotato.kampus.domain.chat.dto.ChatMessageSnapshot;

public record ChatMessageResponse(
	Long id,
	Long chatroomId,
	Long senderId,
	String content,
	boolean isRead,
	boolean isMine
) {
	public static ChatMessageResponse from(ChatMessageSnapshot snapshot) {
		return new ChatMessageResponse(
			snapshot.id(),
			snapshot.chatroomId(),
			snapshot.senderId(),
			snapshot.content(),
			snapshot.isRead(),
			snapshot.isMine()
		);
	}
}