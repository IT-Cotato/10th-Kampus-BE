package com.cotato.kampus.domain.board.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;

public record UniversityBoardResponse(
	String boardName
) {
	public static UniversityBoardResponse from(BoardDto boarDto) {
		return new UniversityBoardResponse(
			boarDto.boardName()
		);
	}
}
