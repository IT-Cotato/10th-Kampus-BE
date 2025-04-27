package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.post.domain.PostReferenceDto;

public record ChatRoomDetailDto(
	Long chatroomId,
	Long postId,
	String postTitle,
	Long boardId,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId
) {
	public static ChatRoomDetailDto of(ChatRoom chatRoom, PostReferenceDto post, Board board) {
		return new ChatRoomDetailDto(
			chatRoom.getId(),
			post.postId(),
			post.title(),
			board.getId(),
			board.getBoardName(),
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