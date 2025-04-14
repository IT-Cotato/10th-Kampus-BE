package com.cotato.kampus.domain.board.domain;

import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardWithFavoriteStatus(
	Long boardId,
	String boardName,
	String description,
	Boolean usesCategories,
	BoardType boardType,
	Boolean isFavorite
) {
	public static BoardWithFavoriteStatus from(Board board, Boolean isFavorite) {
		return new BoardWithFavoriteStatus(
			board.getId(),
			board.getBoardName(),
			board.getDescription(),
			board.getUsesCategories(),
			board.getBoardType(),
			isFavorite
		);
	}
}