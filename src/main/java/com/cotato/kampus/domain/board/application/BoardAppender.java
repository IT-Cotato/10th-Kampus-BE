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
	public Long appendBoard(String boardName, String description, Boolean isCategoryRequired) {
		Board board = Board.builder()
			.boardName(boardName)
			.description(description)
			.boardType(BoardType.GENERAL)
			.isCategoryRequired(isCategoryRequired)
			.boardStatus(BoardStatus.ACTIVE)
			.build();

		return boardRepository.save(board).getId();
	}

	@Transactional
	public Long appendUniversityBoard(String boardName, String description, Long universityId, Boolean isCategoryRequired) {
		Board board = Board.builder()
			.boardName(boardName)
			.description(description)
			.boardType(BoardType.UNIVERSITY)
			.universityId(universityId)
			.isCategoryRequired(isCategoryRequired)
			.boardStatus(BoardStatus.ACTIVE)
			.build();

		return boardRepository.save(board).getId();
	}
}
