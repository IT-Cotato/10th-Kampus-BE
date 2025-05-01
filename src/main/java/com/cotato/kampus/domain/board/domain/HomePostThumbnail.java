package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.post.domain.Post;

public record HomePostThumbnail(
	Long boardId,
	String boardName,
	Long postId,
	String postTitle
) {
	public static HomePostThumbnail from(Board board, Post post) {
		return new HomePostThumbnail(
			board.getId(),
			board.getBoardName(),
			post != null ? post.getId() : null,
			post != null ? post.getTitle() : null
		);
	}
}
