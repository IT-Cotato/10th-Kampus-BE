package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;

public record AdminBoardDetail(
	Long boardId,
	String boardName,
	Long universityId,
	String description,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	Long postCount,
	Long deletionCountdown

) {
	public static AdminBoardDetail of(BoardDto boardDto, Long postCount, Long deletionCountdown){
		return new AdminBoardDetail(
			boardDto.boardId(),
			boardDto.boardName(),
			boardDto.universityId(),
			boardDto.description(),
			boardDto.isCategoryRequired(),
			boardDto.boardStatus(),
			postCount,
			deletionCountdown
		);
	}
}
