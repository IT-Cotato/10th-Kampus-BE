package com.cotato.kampus.domain.board.api.response;

import com.cotato.kampus.domain.board.domain.BoardWithFavoriteStatus;

public record BoardWithDescriptionResponse(
	BoardWithFavoriteStatus boardWithFavoriteStatus
) {
	public static BoardWithDescriptionResponse from(BoardWithFavoriteStatus boardWithFavoriteStatus) {
		return new BoardWithDescriptionResponse(boardWithFavoriteStatus);
	}
}
