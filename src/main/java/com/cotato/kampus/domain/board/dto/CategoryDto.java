package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.board.domain.Category;

public record CategoryDto(
	String categoryName,
	Long boardId
) {
	public static CategoryDto from(Category category) {
		return new CategoryDto(
			category.getCategoryName(),
			category.getBoardId()
		);
	}
}
