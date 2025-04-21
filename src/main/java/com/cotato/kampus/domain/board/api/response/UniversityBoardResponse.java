package com.cotato.kampus.domain.board.api.response;

import com.cotato.kampus.domain.board.domain.Board;

public record UniversityBoardResponse(
	String boardName
) {
	public static UniversityBoardResponse from(Board boarDto) {
		return new UniversityBoardResponse(
			boarDto.getBoardName()
		);
	}
}
