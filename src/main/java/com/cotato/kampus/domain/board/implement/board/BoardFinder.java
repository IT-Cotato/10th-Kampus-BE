package com.cotato.kampus.domain.board.implement.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.Board;
import com.cotato.kampus.domain.board.implement.boardFavorite.BoardFavoriteFinder;
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

	private static final List<BoardType> EXCLUDED_BOARD_TYPES_FOR_FAVORITE_PREVIEW = List.of(
		BoardType.CARDNEWS,
		BoardType.TRENDING
	);

	private final BoardRepository boardRepository;
	private final BoardFavoriteFinder boardFavoriteFinder;

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

	public List<Board> findFavoriteBoardsForPreview(Long userId) {
		Set<Long> favoritesBoardIds = boardFavoriteFinder.findFavoriteBoardIds(userId);
		List<Board> favoriteBoards = boardRepository.findAllByIdIn(favoritesBoardIds);

		return favoriteBoards.stream()
			.filter(board -> !EXCLUDED_BOARD_TYPES_FOR_FAVORITE_PREVIEW.contains(board.getBoardType()))
			.toList();
	}

	public List<Board> findPublicBoards() {
		return boardRepository.findAllByUniversityIdIsNullAndBoardStatus(BoardStatus.ACTIVE);
	}

	public Board findBoard(Long boardId) {
		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

		return board;
	}

	public Optional<Board> findByUniversityId(Long universityId) {
		return boardRepository.findByUniversityId(universityId);
	}

	public Board findUniversityBoard(Long userUniversityId) {
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

	public List<Long> findDefaultFavoriteBoardIds() {
		List<BoardType> targetTypes = List.of(BoardType.FIXED, BoardType.CARDNEWS);
		return boardRepository.findBoardIdsByBoardTypeIn(targetTypes);
	}

	public Long findUniversityBoardId(Long universityId) {
		return boardRepository.findBoardIdByUniversityId(universityId)
			.orElse(null);
	}

	public BoardType findBoardType(Long boardId) {
		return BoardType.valueOf(boardRepository.findBoardTypeByBoardId(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND)));
	}
}