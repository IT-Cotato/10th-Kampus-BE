package com.cotato.kampus.domain.board.application;

import java.time.LocalDateTime;
import java.util.List;

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
	private final BoardFinder boardFinder;

	@Transactional
	public Long update(Long boardId, String boardName, String description, Boolean isCategoryRequired) {
		Board board =  boardFinder.findBoard(boardId);

		board.update(boardName, description, isCategoryRequired);

		boardRepository.save(board);

		return board.getId();
	}

	@Transactional
	public void inactiveBoard(Long boardId) {
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.INACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_INACTIVE);

		board.updateStatus(BoardStatus.INACTIVE);
		boardRepository.save(board);
	}

	@Transactional
	public void activeBoard(Long boardId) {
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_ACTIVE);

		board.updateStatus(BoardStatus.ACTIVE);
		boardRepository.save(board);
	}

	@Transactional
	public void pendingBoard(Long boardId){
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.PENDING_DELETION){
			throw new AppException(ErrorCode.BOARD_ALREADY_PENDING);
		}

		board.updateStatus(BoardStatus.PENDING_DELETION);
		board.setDeletionScheduledAt(LocalDateTime.now().plusDays(30));
	}

	@Transactional
	public void deleteExpiredBoards(){
		LocalDateTime now = LocalDateTime.now();
		List<Board> expiredBoards = boardRepository.findByDeletionScheduledAtBefore(now);

		boardRepository.deleteAll(expiredBoards);
	}
}