package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAppender {
	private final BoardRepository boardRepository;

	@Transactional
	public Long appendBoard(String boardName, String description, Long universityId, Boolean usesCategories) {
		BoardType boardType = (universityId != null) ? BoardType.UNIVERSITY : BoardType.GENERAL;
		Board board = Board.builder()
			.boardName(boardName)
			.description(description)
			.boardType(boardType)
			.usesCategories(usesCategories)
			.boardStatus(BoardStatus.ACTIVE)
			.build();

		return boardRepository.save(board).getId();
	}
}
