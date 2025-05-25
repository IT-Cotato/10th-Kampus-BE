package com.cotato.kampus.domain.board.api.response;

import com.cotato.kampus.domain.board.domain.BoardWithFavoriteStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record UniversityBoardResponse(
	Long boardId,
	String boardName,
	String description,
	Boolean usesCategories,
	BoardType boardType,
	Boolean isFavorite
) {
	public static UniversityBoardResponse from(BoardWithFavoriteStatus boardWithFavoriteStatus) {
		return new UniversityBoardResponse(
			boardWithFavoriteStatus.boardId(),
			boardWithFavoriteStatus.boardName(),
			boardWithFavoriteStatus.description(),
			boardWithFavoriteStatus.usesCategories(),
			boardWithFavoriteStatus.boardType(),
			boardWithFavoriteStatus.isFavorite()
		);
	}
}
