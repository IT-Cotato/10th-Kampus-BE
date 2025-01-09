package com.cotato.kampus.domain.board.dto.response;

public record BoardResponse(
	Long boardId
) {
	public static BoardResponse of(Long boardId) {
		return new BoardResponse(boardId);
	}
}
