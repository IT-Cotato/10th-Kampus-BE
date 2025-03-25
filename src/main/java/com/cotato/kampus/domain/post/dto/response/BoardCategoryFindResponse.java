package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.CategoryDto;

public record BoardCategoryFindResponse(
	List<String> categories
) {
	public static BoardCategoryFindResponse from (List<CategoryDto> categoryDtos) {
		List<String> categories = categoryDtos.stream()
			.map(CategoryDto::categoryName)
			.toList();

		return new BoardCategoryFindResponse(categories);
	}
}