package com.cotato.kampus.domain.board.implement.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.port.BoardRepository;
import com.cotato.kampus.domain.board.enums.BoardStatus;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFinder {

	private final BoardRepository boardRepository;

	public List<Board> findAllBoards(BoardStatus boardStatus) {
		List<Board> boards;

		// 전체 게시판 조회 (카드뉴스 제외)
		if (boardStatus == null) {
			boards = boardRepository.findAll().stream()
				.filter(board -> !board.getBoardType().equals(BoardType.CARDNEWS))
				.toList();
		} else {
			boards = boardRepository.findAllByBoardStatus(boardStatus).stream()
				.filter(board -> !board.getBoardType().equals(BoardType.CARDNEWS))
				.toList();
		}

		return boards;
	}

	public List<Board> findBoardsWithIds(List<Long> boardIds) {
		return boardRepository.findAllByIdIn(boardIds);
	}

	public Map<Long, Board> findBoardMap(List<Long> boardIds) {
		return findBoardsWithIds(boardIds).stream()
			.collect(Collectors.toMap(
				Board::getId,
				Function.identity()
			));
	}

	public List<Board> findPublicBoards() {
		return boardRepository.findAllByUniversityIdIsNullAndBoardStatus(BoardStatus.ACTIVE);
	}

	public Board findBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board;
	}

	public Board findUserUniversityBoard(Long userUniversityId) {
		Board board = boardRepository.findByUniversityId(userUniversityId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
		return board;
	}

	public Long findCardNewsBoardId() {
		Board board = boardRepository.findByBoardType(BoardType.CARDNEWS)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board.getId();
	}

	public List<Long> findExpiredBoardIds(LocalDateTime now) {
		List<Board> expiredBoards = boardRepository.findByDeletionScheduledAtBefore(now);

		return expiredBoards.stream().map(Board::getId).toList();
	}

}