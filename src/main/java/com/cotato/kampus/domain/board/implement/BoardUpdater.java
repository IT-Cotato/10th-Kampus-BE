package com.cotato.kampus.domain.board.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
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

		Board updatedBoard = board.withUpdateInfo(boardName, description, isCategoryRequired);

		boardRepository.save(updatedBoard);

		return updatedBoard.getId();
	}

	@Transactional
	public void inactiveBoard(Long boardId) {
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.INACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_INACTIVE);

		Board updatedBoard = board.withBoardStatus(BoardStatus.INACTIVE);
		boardRepository.save(updatedBoard);
	}

	@Transactional
	public void activeBoard(Long boardId) {
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_ACTIVE);

		Board updatedBoard = board.withBoardStatus(BoardStatus.ACTIVE);
		boardRepository.save(updatedBoard);
	}

	@Transactional
	public void pendingBoard(Long boardId){
		Board board =  boardFinder.findBoard(boardId);

		if(board.getBoardStatus() == BoardStatus.PENDING_DELETION){
			throw new AppException(ErrorCode.BOARD_ALREADY_PENDING);
		}

		Board updatedBoard = board.withPendingInfo(BoardStatus.PENDING_DELETION, LocalDateTime.now().plusDays(30));
		boardRepository.save(updatedBoard);
	}

	@Transactional
	public void deleteExpiredBoards(){
		LocalDateTime now = LocalDateTime.now();
		List<Board> expiredBoardEntities = boardRepository.findByDeletionScheduledAtBefore(now);

		boardRepository.deleteAll(expiredBoardEntities);
	}
}