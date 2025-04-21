package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.domain.UniversityBoard;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

public record AdminBoardDetail(
	Long boardId,
	String boardName,
	Long universityId,
	String description,
	BoardType boardType,
	Boolean usesCategories,
	BoardStatus boardStatus,
	Long postCount,
	Long deletionCountdown

) {
	public static AdminBoardDetail of(Board board, Long postCount, Long deletionCountdown) {
		Long universityId = null;
		if (board instanceof UniversityBoard) {
			universityId = ((UniversityBoard) board).getUniversityId();
		}

		return new AdminBoardDetail(
			board.getId(),
			board.getBoardName(),
			universityId,
			board.getDescription(),
			board.getBoardType(),
			board.getUsesCategories(),
			board.getBoardStatus(),
			postCount,
			deletionCountdown
		);
	}
}
