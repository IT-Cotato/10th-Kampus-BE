package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.board.dao.entity.BoardCategory;

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
