package com.cotato.kampus.domain.post.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.domain.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

public record MyPostWithPhoto(
	Long id,
	Long boardId,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	String thumbnailUrl) {

	public static MyPostWithPhoto from(Post post, Board board, PostPhoto postPhoto) {
		return new MyPostWithPhoto(
			post.getId(),
			board.getId(),
			board.getBoardName(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			post.getCreatedTime(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}