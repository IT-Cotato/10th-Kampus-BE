package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.post.dto.PostDto;

public record HomeBoardAndPostPreview(
	Long boardId,
	String boardName,
	Long id,
	String postTitle
) {
	public static HomeBoardAndPostPreview from(BoardDto boardDto, PostDto postDto) {
		return new HomeBoardAndPostPreview(
			boardDto.boardId(),
			boardDto.boardName(),
			postDto != null ? postDto.id() : null,
			postDto != null ? postDto.title() : null
		);
	}
}
