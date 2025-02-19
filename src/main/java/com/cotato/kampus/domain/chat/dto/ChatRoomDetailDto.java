package com.cotato.kampus.domain.chat.dto;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.chat.domain.Chatroom;
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
	public static ChatRoomDetailDto of(Chatroom chatroom, PostReferenceDto post, BoardDto board) {
		return new ChatRoomDetailDto(
			chatroom.getId(),
			post.postId(),
			post.title(),
			board.boardId(),
			board.boardName(),
			chatroom.getInitialSenderId(),
			chatroom.getInitialReceiverId()
		);
	}

	public static ChatRoomDetailDto ofDeleted(Chatroom chatroom, PostReferenceDto post) {
		return new ChatRoomDetailDto(
			chatroom.getId(),
			post.postId(),
			post.title(),
			-1L,
			"[삭제된 게시판]",
			chatroom.getInitialSenderId(),
			chatroom.getInitialReceiverId()
		);
	}
}