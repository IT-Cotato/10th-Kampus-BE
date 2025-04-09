package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.post.dto.PostDto;

public record HomeBoardAndPostPreview(
	Long boardId,
	String boardName,
	Long id,
	String postTitle
) {
	public static HomeBoardAndPostPreview from(Board board, PostDto postDto) {
		return new HomeBoardAndPostPreview(
			board.getId(),
			board.getBoardName(),
			postDto != null ? postDto.id() : null,
			postDto != null ? postDto.title() : null
		);
	}
}
