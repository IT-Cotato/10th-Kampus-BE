package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.post.domain.Post;

public record HomeBoardAndPostPreview(
	Long boardId,
	String boardName,
	Long id,
	String postTitle
) {
	public static HomeBoardAndPostPreview from(Board board, Post post) {
		return new HomeBoardAndPostPreview(
			board.getId(),
			board.getBoardName(),
			post != null ? post.getId() : null,
			post != null ? post.getTitle() : null
		);
	}
}
