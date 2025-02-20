package com.cotato.kampus.domain.board.dto.response;

import java.util.List;

import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatus;

public record BoardListResponse(
	List<BoardWithFavoriteStatus> boards
) {
	public static BoardListResponse from(List<BoardWithFavoriteStatus> boards){
		return new BoardListResponse(boards);
	}
}
