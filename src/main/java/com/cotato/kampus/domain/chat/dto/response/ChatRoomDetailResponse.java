package com.cotato.kampus.domain.chat.dto.response;

import com.cotato.kampus.domain.chat.dto.ChatRoomDetailDto;

public record ChatRoomDetailResponse(
	Long chatroomId,
	Long postId,
	String postTitle,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId
) {
	public static ChatRoomDetailResponse from(ChatRoomDetailDto dto) {
		return new ChatRoomDetailResponse(
			dto.chatroomId(),
			dto.postId(),
			dto.postTitle(),
			dto.boardName(),
			dto.initialSenderId(),
			dto.initialReceiverId()
		);
	}
}