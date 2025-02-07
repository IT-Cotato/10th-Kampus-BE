package com.cotato.kampus.domain.chat.dto;

public record ChatMessageSliceWithUserId(
	// 채팅 메시지를 조회한 유저의 id
	Long currentUserId,
	ChatMessageSlice messages
) {
	public static ChatMessageSliceWithUserId from(Long currentUserId, ChatMessageSlice messages) {
		return new ChatMessageSliceWithUserId(currentUserId, messages);
	}
}