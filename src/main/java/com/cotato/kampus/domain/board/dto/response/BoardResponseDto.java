package com.cotato.kampus.domain.board.dto.response;

import java.util.List;

public class BoardResponseDto {

	public record BoardDto(Long boardId, String boardName, boolean isFavorite){
		public static BoardDto of(Long boardId, String boardName, boolean isFavorite) {
			return new BoardDto(boardId, boardName, isFavorite);
		}
	}
	public record BoardListDto(List<BoardDto> boards){
		public static BoardListDto of(List<BoardDto> boards){
			return new BoardListDto(boards);
		}
	}
}
