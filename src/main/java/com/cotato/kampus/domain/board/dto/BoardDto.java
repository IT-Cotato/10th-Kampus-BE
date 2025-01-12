package com.cotato.kampus.domain.board.dto;

public record BoardDto(
	Long boardId,
	String boardName
) {
	public static BoardDto of(Long boardId, String boardName) {
		return new BoardDto(boardId, boardName);
	}
}
