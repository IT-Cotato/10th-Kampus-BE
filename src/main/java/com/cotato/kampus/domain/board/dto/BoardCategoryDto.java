package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.board.domain.BoardCategory;

public record BoardCategoryDto(
	Long categoryId,
	String categoryName,
	Long boardId
) {
	public static BoardCategoryDto from(BoardCategory boardCategory) {
		return new BoardCategoryDto(
			boardCategory.getId(),
			boardCategory.getCategoryName(),
			boardCategory.getBoardId()
		);
	}
}
