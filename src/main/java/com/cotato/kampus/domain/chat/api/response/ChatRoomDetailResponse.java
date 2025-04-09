package com.cotato.kampus.domain.chat.api.response;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;

public record ChatRoomDetailResponse(
	Long chatroomId,
	Long postId,
	String postTitle,
	Long boardId,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId,
	boolean isPostDeleted
) {
	public static ChatRoomDetailResponse from(ChatRoomDetailDto dto) {
		return new ChatRoomDetailResponse(
			dto.chatroomId(),
			dto.postId(),
			dto.postTitle(),
			dto.boardId(),
			dto.boardName(),
			dto.initialSenderId(),
			dto.initialReceiverId(),
			dto.postId() == -1L
		);
	}
}