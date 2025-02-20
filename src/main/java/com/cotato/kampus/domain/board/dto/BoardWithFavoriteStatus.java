package com.cotato.kampus.domain.board.dto;

public record BoardWithFavoriteStatus(
	Long boardId,
	String boardName,
	String description,
	Boolean isFavorite
) {
	public static BoardWithFavoriteStatus from(BoardDto boardDto, Boolean isFavorite) {
		return new BoardWithFavoriteStatus(
			boardDto.boardId(),
			boardDto.boardName(),
			boardDto.description(),
			isFavorite
		);
	}
}