package com.cotato.kampus.domain.board.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;

public record BoardListResponse(
	List<BoardWithFavoriteStatusDto> boards
) {
	public static BoardListResponse from(List<BoardWithFavoriteStatusDto> boards){
		return new BoardListResponse(boards);
	}
}
