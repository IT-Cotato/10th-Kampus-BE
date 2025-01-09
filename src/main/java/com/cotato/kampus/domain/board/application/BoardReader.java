package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatusDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReader {
	private final BoardRepository boardRepository;

	public List<BoardWithFavoriteStatusDto> readAll(){
		return boardRepository.findAll().stream()
			.map(board -> BoardWithFavoriteStatusDto.of(
				board.getId(),
				board.getBoardName(),
				false
			))
			.toList();
	}
}