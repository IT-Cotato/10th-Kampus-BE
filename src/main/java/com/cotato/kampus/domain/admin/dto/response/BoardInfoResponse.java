package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardInfoResponse(
	Long boardId,
	String boardName,
	String description,
	Boolean isUniversityBoard,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	BoardType boardType
) {
	public static BoardInfoResponse from(BoardDto board) {
		return new BoardInfoResponse(
			board.boardId(),
			board.boardName(),
			board.description(),
			board.universityId() != null,
			board.isCategoryRequired(),
			board.boardStatus(),
			board.boardType()
		);
	}
}