package com.cotato.kampus.domain.board.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardRepository;
import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.dto.BoardWithFavoriteStatus;
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

	public List<BoardDto> findAllBoards(BoardStatus boardStatus) {
		List<BoardDto> boards;

		// 전체 게시판 조회 (카드뉴스 제외)
		if (boardStatus == null) {
			boards = boardRepository.findAll().stream()
				.filter(board -> !board.getBoardType().equals(BoardType.CARDNEWS))
				.map(BoardDto::from)
				.toList();
		} else {
			boards = boardRepository.findAllByBoardStatus(boardStatus).stream()
				.filter(board -> !board.getBoardType().equals(BoardType.CARDNEWS))
				.map(BoardDto::from)
				.toList();
		}

		return boards;
	}

	public List<BoardDto> findPublicBoards() {
		return boardRepository.findAllByUniversityIdIsNullAndBoardStatus(BoardStatus.ACTIVE).stream()
			.map(BoardDto::from)
			.toList();
	}

	public Board findBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board;
	}

	public BoardDto findBoardDto(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return BoardDto.from(board);
	}

	public BoardDto findUserUniversityBoard(Long userUniversityId) {
		Board board = boardRepository.findByUniversityId(userUniversityId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
		return BoardDto.from(board);
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