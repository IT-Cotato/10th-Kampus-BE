package com.cotato.kampus.domain.board.dto.response;

public record FavoriteBoardResponse(
	Long boardId
) {
	public static FavoriteBoardResponse of(Long boardId) {
		return new FavoriteBoardResponse(boardId);
	}
}