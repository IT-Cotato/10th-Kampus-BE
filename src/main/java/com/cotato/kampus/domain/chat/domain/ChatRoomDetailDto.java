package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.post.dto.PostReferenceDto;

public record ChatRoomDetailDto(
	Long chatroomId,
	Long postId,
	String postTitle,
	Long boardId,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId
) {
	public static ChatRoomDetailDto of(ChatRoomEntity chatRoomEntity, PostReferenceDto post, BoardDto board) {
		return new ChatRoomDetailDto(
			chatRoomEntity.getId(),
			post.postId(),
			post.title(),
			board.boardId(),
			board.boardName(),
			chatRoomEntity.getInitialSenderId(),
			chatRoomEntity.getInitialReceiverId()
		);
	}

	public static ChatRoomDetailDto ofDeleted(ChatRoomEntity chatRoomEntity, PostReferenceDto post) {
		return new ChatRoomDetailDto(
			chatRoomEntity.getId(),
			post.postId(),
			post.title(),
			-1L,
			"",
			chatRoomEntity.getInitialSenderId(),
			chatRoomEntity.getInitialReceiverId()
		);
	}
}