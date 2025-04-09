package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

public record BoardCategoryDto(
	Long categoryId,
	String categoryName,
	Long boardId
) {
	public static BoardCategoryDto from(BoardCategoryEntity boardCategoryEntity) {
		return new BoardCategoryDto(
			boardCategoryEntity.getId(),
			boardCategoryEntity.getCategoryName(),
			boardCategoryEntity.getBoardId()
		);
	}
}
