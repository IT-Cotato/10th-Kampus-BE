package com.cotato.kampus.domain.board.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;

public record BoardWithDescriptionResponse(
	String boardName,
	String description
) {
	public static BoardWithDescriptionResponse from(BoardDto boardDto) {
		return new BoardWithDescriptionResponse(
			boardDto.boardName(),
			boardDto.description()
		);
	}
}
