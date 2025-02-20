package com.cotato.kampus.domain.board.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record BoardDto(
	Long boardId,
	String boardName,
	String description,
	Long universityId,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	BoardType boardType,
	LocalDateTime deletionScheduledAt
) {
	public static BoardDto from(Board board) {
		return new BoardDto(
			board.getId(),
			board.getBoardName(),
			board.getDescription(),
			board.getUniversityId(),
			board.getIsCategoryRequired(),
			board.getBoardStatus(),
			board.getBoardType(),
			board.getDeletionScheduledAt()
		);
	}
}
