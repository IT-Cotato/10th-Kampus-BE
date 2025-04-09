package com.cotato.kampus.domain.board.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.repository.BoardRepository;
import com.cotato.kampus.domain.board.dao.entity.BoardEntity;
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
		BoardEntity boardEntity =  boardFinder.findBoard(boardId);

		boardEntity.update(boardName, description, isCategoryRequired);

		boardRepository.save(boardEntity);

		return boardEntity.getId();
	}

	@Transactional
	public void inactiveBoard(Long boardId) {
		BoardEntity boardEntity =  boardFinder.findBoard(boardId);

		if(boardEntity.getBoardStatus() == BoardStatus.INACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_INACTIVE);

		boardEntity.updateStatus(BoardStatus.INACTIVE);
		boardRepository.save(boardEntity);
	}

	@Transactional
	public void activeBoard(Long boardId) {
		BoardEntity boardEntity =  boardFinder.findBoard(boardId);

		if(boardEntity.getBoardStatus() == BoardStatus.ACTIVE)
			throw new AppException(ErrorCode.BOARD_ALREADY_ACTIVE);

		boardEntity.updateStatus(BoardStatus.ACTIVE);
		boardRepository.save(boardEntity);
	}

	@Transactional
	public void pendingBoard(Long boardId){
		BoardEntity boardEntity =  boardFinder.findBoard(boardId);

		if(boardEntity.getBoardStatus() == BoardStatus.PENDING_DELETION){
			throw new AppException(ErrorCode.BOARD_ALREADY_PENDING);
		}

		boardEntity.updateStatus(BoardStatus.PENDING_DELETION);
		boardEntity.setDeletionScheduledAt(LocalDateTime.now().plusDays(30));
	}

	@Transactional
	public void deleteExpiredBoards(){
		LocalDateTime now = LocalDateTime.now();
		List<BoardEntity> expiredBoardEntities = boardRepository.findByDeletionScheduledAtBefore(now);

		boardRepository.deleteAll(expiredBoardEntities);
	}
}