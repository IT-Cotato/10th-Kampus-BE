package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdater {

	private final BoardRepository boardRepository;

	public Long update(Long boardId, String boardName, String description, Boolean isCategoryRequired) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		board.update(boardName, description, isCategoryRequired);

		boardRepository.save(board);

		return board.getId();
	}

	public void inactiveBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		if(board.getBoardStatus() == BoardStatus.INACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_INACTIVE);

		board.inactive();
		boardRepository.save(board);
	}

	public void activeBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		if(board.getBoardStatus() == BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_ACTIVE);

		board.active();
		boardRepository.save(board);
	}
}
