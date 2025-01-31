package com.cotato.kampus.domain.board.dto.response;

import com.cotato.kampus.domain.board.dto.BoardDto;

public record BoardResponse(
	BoardDto boardDto
) {
	public static BoardResponse from(BoardDto boardDto) {
		return new BoardResponse(boardDto);
	}
}
