package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;

public record AdminBoardDetail(
	Long boardId,
	String boardName,
	Long universityId,
	String description,
	Boolean usesCategories,
	BoardStatus boardStatus,
	Long postCount,
	Long deletionCountdown

) {
	public static AdminBoardDetail of(Board board, Long postCount, Long deletionCountdown){
		return new AdminBoardDetail(
			board.getId(),
			board.getBoardName(),
			board.getUniversityId(),
			board.getDescription(),
			board.getUsesCategories(),
			board.getBoardStatus(),
			postCount,
			deletionCountdown
		);
	}
}
