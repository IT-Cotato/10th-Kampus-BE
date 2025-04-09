package com.cotato.kampus.domain.board.api.response;

import com.cotato.kampus.domain.board.domain.BoardDto;

public record UniversityBoardResponse(
	String boardName
) {
	public static UniversityBoardResponse from(BoardDto boarDto) {
		return new UniversityBoardResponse(
			boarDto.boardName()
		);
	}
}
