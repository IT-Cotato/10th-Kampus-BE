package com.cotato.kampus.domain.chat.api.response;

import com.cotato.kampus.domain.chat.domain.ChatMessageSnapshot;

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