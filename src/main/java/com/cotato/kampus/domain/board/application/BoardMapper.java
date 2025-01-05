package com.cotato.kampus.domain.board.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.response.BoardResponseDto;

public class BoardMapper {

	public static List<BoardResponseDto.BoardDto> toBoardDtos(List<Board> boards, Set<Long> favoriteBoardIds) {
		return boards.stream()
			.map(board -> BoardResponseDto.BoardDto.of(
				board.getId(),
				board.getBoardName(),
				favoriteBoardIds.contains(board.getId())
			))
			.collect(Collectors.toList());
	}
}