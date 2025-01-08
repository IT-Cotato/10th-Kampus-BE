package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import com.cotato.kampus.domain.board.dto.BoardDto;

public class BoardDtoEnhancer {

	public static List<BoardDto> updateFavoriteStatus(List<BoardDto> boardDtos, Set<Long> favoriteBoardIds) {
		return boardDtos.stream()
			.map(boardDto -> BoardDto.of(
				boardDto.boardId(),
				boardDto.boardName(),
				favoriteBoardIds.contains(boardDto.boardId())
			))
			.toList();
	}

	public static List<BoardDto> filterFavoriteBoards(List<BoardDto> boardDtos, Set<Long> favoriteBoardIds) {
		return boardDtos.stream()
			.filter(boardDto -> favoriteBoardIds.contains(boardDto.boardId()))
			.map(boardDto -> BoardDto.of(
				boardDto.boardId(),
				boardDto.boardName(),
				true
			))
			.toList();
	}
}