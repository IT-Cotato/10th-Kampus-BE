package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.BoardDto;

public class BoardMapper {

	public static List<BoardDto> toBoardDtos(List<Board> boards, Set<Long> favoriteBoardIds) {
		return boards.stream()
			.map(board -> BoardDto.of(
				board.getId(),
				board.getBoardName(),
				favoriteBoardIds.contains(board.getId())
			))
			.toList();
	}
}