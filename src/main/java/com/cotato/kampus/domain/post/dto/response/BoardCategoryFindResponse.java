package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.BoardCategoryDto;

public record BoardCategoryFindResponse(
	List<String> categories
) {
	public static BoardCategoryFindResponse from (List<BoardCategoryDto> boardCategoryDtos) {
		List<String> categories = boardCategoryDtos.stream()
			.map(BoardCategoryDto::categoryName)
			.toList();

		return new BoardCategoryFindResponse(categories);
	}
}