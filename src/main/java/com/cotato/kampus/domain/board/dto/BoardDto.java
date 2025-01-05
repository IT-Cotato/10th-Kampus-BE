package com.cotato.kampus.domain.board.dto;

public record BoardDto(Long boardId, String boardName, boolean isFavorite){
	public static BoardDto of(Long boardId, String boardName, boolean isFavorite) {
		return new BoardDto(boardId, boardName, isFavorite);
	}
}