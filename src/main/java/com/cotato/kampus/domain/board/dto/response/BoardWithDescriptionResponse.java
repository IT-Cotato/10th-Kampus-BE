package com.cotato.kampus.domain.board.dto.response;

import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatus;

public record BoardWithDescriptionResponse(
	Long boardId,
	String boardName,
	String description,
	Boolean isFavorite
) {
	public static BoardWithDescriptionResponse from(BoardWithFavoriteStatus boardWithFavoriteStatus) {
		return new BoardWithDescriptionResponse(
			boardWithFavoriteStatus.boardId(),
			boardWithFavoriteStatus.boardName(),
			boardWithFavoriteStatus.description(),
			boardWithFavoriteStatus.isFavorite()
		);
	}
}
