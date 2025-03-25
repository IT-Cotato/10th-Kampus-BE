package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.board.domain.BoardCategory;

public record CategoryDto(
	String categoryName,
	Long boardId
) {
	public static CategoryDto from(BoardCategory boardCategory) {
		return new CategoryDto(
			boardCategory.getCategoryName(),
			boardCategory.getBoardId()
		);
	}
}
