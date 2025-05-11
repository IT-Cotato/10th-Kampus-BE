package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.board.domain.Board;

public record ChatRoomDetailDto(
	Long chatroomId,
	Long referenceId,
	String title,
	Long boardId,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId,
	boolean isDeleted) {
	public static ChatRoomDetailDto of(ChatRoom chatRoom, ChatReference reference, Board board) {
		return new ChatRoomDetailDto(
			chatRoom.getId(),
			reference.getReferenceId(),
			reference.getTitle(),
			board != null ? board.getId() : -1L,
			board != null ? board.getBoardName() : "",
			chatRoom.getInitialSenderId(),
			chatRoom.getInitialReceiverId(),
			false);
	}

	public static ChatRoomDetailDto ofDeleted(ChatRoom chatRoom, ChatReference reference) {
		return new ChatRoomDetailDto(
			chatRoom.getId(),
			reference.getReferenceId(),
			reference.getTitle(),
			-1L,
			"",
			chatRoom.getInitialSenderId(),
			chatRoom.getInitialReceiverId(),
			true);
	}
}