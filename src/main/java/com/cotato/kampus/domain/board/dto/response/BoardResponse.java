package com.cotato.kampus.domain.board.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.BoardDto;

public record BoardResponse(List<BoardDto> boards) {
	public static BoardResponse of(List<BoardDto> boards){
		return new BoardResponse(boards);
	}
}
