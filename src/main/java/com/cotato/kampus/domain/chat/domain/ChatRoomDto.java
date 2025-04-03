package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;

public record ChatRoomDto(
	Long chatRoomId,
	Long postId,
	Long senderId,
	Long receiverId
) {
	public static ChatRoomDto from(ChatRoomEntity chatRoomEntity) {
		return new ChatRoomDto(
			chatRoomEntity.getId(),
			chatRoomEntity.getPostId(),
			chatRoomEntity.getInitialSenderId(),
			chatRoomEntity.getInitialReceiverId()
		);
	}
}