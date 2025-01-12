package com.cotato.kampus.domain.board.dto;

public record BoardWithFavoriteStatusDto(
	Long boardId,
	String boardName,
	boolean isFavorite
){
	public static BoardWithFavoriteStatusDto of(Long boardId, String boardName, boolean isFavorite) {
		return new BoardWithFavoriteStatusDto(boardId, boardName, isFavorite);
	}
}