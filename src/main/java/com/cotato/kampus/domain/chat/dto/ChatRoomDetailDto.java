package com.cotato.kampus.domain.chat.dto;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.post.dto.PostDto;

public record ChatRoomDetailDto(
	Long chatroomId,
	Long postId,
	String postTitle,
	String boardName,
	Long initialSenderId,
	Long initialReceiverId
) {
	public static ChatRoomDetailDto of(Chatroom chatroom, PostDto post, BoardDto board) {
		return new ChatRoomDetailDto(
			chatroom.getId(),
			post.id(),
			post.title(),
			board.boardName(),
			chatroom.getInitialSenderId(),
			chatroom.getInitialReceiverId()
		);
	}
}