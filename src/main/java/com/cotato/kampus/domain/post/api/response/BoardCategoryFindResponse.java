package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import com.cotato.kampus.domain.board.domain.BoardCategory;

public record BoardCategoryFindResponse(
	List<String> categories
) {
	public static BoardCategoryFindResponse from (List<BoardCategory> boardCategoriess) {
		List<String> categories = boardCategoriess.stream()
			.map(BoardCategory::getCategoryName)
			.toList();

		return new BoardCategoryFindResponse(categories);
	}
}