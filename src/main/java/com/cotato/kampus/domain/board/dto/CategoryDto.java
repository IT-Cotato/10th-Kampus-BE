package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.board.domain.BoardCategory;

public record CategoryDto(
	Long categoryId,
	String categoryName,
	Long boardId
) {
	public static CategoryDto from(BoardCategory boardCategory) {
		return new CategoryDto(
			boardCategory.getId(),
			boardCategory.getCategoryName(),
			boardCategory.getBoardId()
		);
	}
}
