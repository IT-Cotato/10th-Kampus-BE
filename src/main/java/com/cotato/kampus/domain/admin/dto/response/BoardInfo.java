package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardInfo(
	Long boardId,
	String boardName,
	String description,
	Boolean isUniversityBoard,
	String universityName,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	BoardType boardType
) {
	public static BoardInfo from(BoardDto board, String universityName){
		return new BoardInfo(
			board.boardId(),
			board.boardName(),
			board.description(),
			board.universityId() != null,
			universityName,
			board.isCategoryRequired(),
			board.boardStatus(),
			board.boardType()
		);
	}
}
