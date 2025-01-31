package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
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
}
