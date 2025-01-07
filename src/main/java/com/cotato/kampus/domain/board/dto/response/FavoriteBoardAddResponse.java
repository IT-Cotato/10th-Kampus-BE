package com.cotato.kampus.domain.board.dto.response;

public record FavoriteBoardAddResponse(
	Long boardId
) {
	public static FavoriteBoardAddResponse of(Long boardId) {
		return new FavoriteBoardAddResponse(boardId);
	}
}
