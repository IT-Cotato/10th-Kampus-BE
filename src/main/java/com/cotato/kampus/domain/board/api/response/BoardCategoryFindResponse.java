package com.cotato.kampus.domain.board.api.response;

import java.util.List;

import com.cotato.kampus.domain.category.domain.Category;

public record BoardCategoryFindResponse(
	List<Category> categories
) {
	public static BoardCategoryFindResponse from (List<Category> boardCategories) {
		return new BoardCategoryFindResponse(boardCategories);
	}
}