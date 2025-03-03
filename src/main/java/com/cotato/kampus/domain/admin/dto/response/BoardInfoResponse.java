package com.cotato.kampus.domain.admin.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardInfoResponse(
	Long boardId,
	String boardName,
	String description,
	Boolean isUniversityBoard,
	String universityName,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	BoardType boardType
) {
	public static BoardInfoResponse from(BoardInfo boardInfo) {
		return new BoardInfoResponse(
			boardInfo.boardId(),
			boardInfo.boardName(),
			boardInfo.description(),
			boardInfo.isUniversityBoard(),
			boardInfo.universityName(),
			boardInfo.isCategoryRequired(),
			boardInfo.boardStatus(),
			boardInfo.boardType()
		);
	}
}