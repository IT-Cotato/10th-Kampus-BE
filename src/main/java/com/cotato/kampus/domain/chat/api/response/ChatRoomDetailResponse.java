package com.cotato.kampus.domain.chat.api.response;

import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;

public record ChatRoomDetailResponse(
	Long chatroomId,
	Long referenceId,
	String title,
	Long boardId,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId,
	boolean isReferenceDeleted) {
	public static ChatRoomDetailResponse from(ChatRoomDetailDto dto) {
		return new ChatRoomDetailResponse(
			dto.chatroomId(),
			dto.referenceId(),
			dto.title(),
			dto.boardId(),
			dto.boardName(),
			dto.initialSenderId(),
			dto.initialReceiverId(),
			dto.isDeleted());
	}
}