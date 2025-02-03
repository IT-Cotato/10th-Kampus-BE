package com.cotato.kampus.domain.chat.dto;

import com.cotato.kampus.domain.chat.domain.Chatroom;

public record ChatRoomDto(
	Long chatRoomId,
	Long postId,
	Long senderId,
	Long receiverId
) {
	public static ChatRoomDto from(Chatroom chatroom) {
		return new ChatRoomDto(
			chatroom.getId(),
			chatroom.getPostId(),
			chatroom.getInitialSenderId(),
			chatroom.getInitialReceiverId()
		);
	}
}