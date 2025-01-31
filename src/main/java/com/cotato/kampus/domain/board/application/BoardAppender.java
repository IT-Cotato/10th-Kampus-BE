package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAppender {
	private final BoardRepository boardRepository;

	@Transactional
	public Long appendBoard(String boardName, Long universityId, Boolean isCategoryRequired) {
		Board board = Board.builder()
			.boardName(boardName)
			.universityId(universityId)
			.isCategoryRequired(isCategoryRequired)
			.build();

		return boardRepository.save(board).getId();
	}
}
