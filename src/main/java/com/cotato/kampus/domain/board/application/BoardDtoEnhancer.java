package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;

public class BoardDtoEnhancer {

	public static List<BoardWithFavoriteStatusDto> updateFavoriteStatus(List<BoardWithFavoriteStatusDto> boardWithFavoriteStatusDtos, Set<Long> favoriteBoardIds) {
		return boardWithFavoriteStatusDtos.stream()
			.map(boardDto -> BoardWithFavoriteStatusDto.of(
				boardDto.boardId(),
				boardDto.boardName(),
				favoriteBoardIds.contains(boardDto.boardId())
			))
			.toList();
	}

	public static List<BoardWithFavoriteStatusDto> filterFavoriteBoards(List<BoardWithFavoriteStatusDto> boardWithFavoriteStatusDtos, Set<Long> favoriteBoardIds) {
		return boardWithFavoriteStatusDtos.stream()
			.filter(boardDto -> favoriteBoardIds.contains(boardDto.boardId()))
			.map(boardDto -> BoardWithFavoriteStatusDto.of(
				boardDto.boardId(),
				boardDto.boardName(),
				true
			))
			.toList();
	}
}