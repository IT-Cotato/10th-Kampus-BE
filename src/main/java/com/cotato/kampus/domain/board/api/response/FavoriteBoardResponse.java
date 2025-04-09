package com.cotato.kampus.domain.board.api.response;

public record FavoriteBoardResponse(
	Long boardId
) {
	public static FavoriteBoardResponse of(Long boardId) {
		return new FavoriteBoardResponse(boardId);
	}
}