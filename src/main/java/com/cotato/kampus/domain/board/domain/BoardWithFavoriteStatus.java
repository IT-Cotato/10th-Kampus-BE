package com.cotato.kampus.domain.board.domain;

public record BoardWithFavoriteStatus(
	Long boardId,
	String boardName,
	String description,
	Boolean isFavorite
) {
	public static BoardWithFavoriteStatus from(Board board, Boolean isFavorite) {
		return new BoardWithFavoriteStatus(
			board.getId(),
			board.getBoardName(),
			board.getDescription(),
			isFavorite
		);
	}
}