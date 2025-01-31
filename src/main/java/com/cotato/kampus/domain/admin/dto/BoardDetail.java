package com.cotato.kampus.domain.admin.dto;

import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardStatus;

public record BoardDetail(
	Long boardId,
	String boardName,
	Long universityId,
	String description,
	Boolean isCategoryRequired,
	BoardStatus boardStatus,
	Long postCount
) {
	public static BoardDetail of(BoardDto boardDto, Long postCount){
		return new BoardDetail(
			boardDto.boardId(),
			boardDto.boardName(),
			boardDto.universityId(),
			boardDto.description(),
			boardDto.isCategoryRequired(),
			boardDto.boardStatus(),
			postCount
		);
	}
}
