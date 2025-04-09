package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.board.domain.BoardDto;
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
	public static ChatRoomDetailDto of(ChatRoom chatRoom, PostReferenceDto post, BoardDto board) {
		return new ChatRoomDetailDto(
			chatRoom.getId(),
			post.postId(),
			post.title(),
			board.boardId(),
			board.boardName(),
			chatRoom.getInitialSenderId(),
			chatRoom.getInitialReceiverId()
		);
	}

	public static ChatRoomDetailDto ofDeleted(ChatRoom chatRoom, PostReferenceDto post) {
		return new ChatRoomDetailDto(
			chatRoom.getId(),
			post.postId(),
			post.title(),
			-1L,
			"",
			chatRoom.getInitialSenderId(),
			chatRoom.getInitialReceiverId()
		);
	}
}