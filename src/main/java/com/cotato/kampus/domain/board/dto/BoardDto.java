package com.cotato.kampus.domain.board.dto;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;

public record BoardDto(
	Long boardId,
	String boardName,
	Long universityId,
	String description,
	Boolean isCategoryRequired,
	BoardStatus boardStatus
) {
	public static BoardDto from(Board board) {
		return new BoardDto(
			board.getId(),
			board.getBoardName(),
			board.getUniversityId(),
			board.getDescription(),
			board.getIsCategoryRequired(),
			board.getBoardStatus());
	}
}
