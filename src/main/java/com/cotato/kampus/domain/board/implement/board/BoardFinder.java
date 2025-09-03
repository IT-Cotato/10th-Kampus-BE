package com.cotato.kampus.domain.board.implement.board;

import java.time.LocalDateTime;
import java.util.EnumSet;
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

	private static final Set<BoardType> EXCLUDED_BOARD_TYPES_FOR_FAVORITE_PREVIEW = EnumSet.of(
		BoardType.CARDNEWS, BoardType.TRENDING, BoardType.UNIVERSITY
	);

	private static final List<BoardType> EXCLUDED_BOARD_TYPES_FOR_ADMIN = List.of(
		BoardType.CARDNEWS
	);

	private static final List<BoardType> DEFAULT_FAVORITE_BOARD_TYPES = List.of(
		BoardType.FIXED, BoardType.CARDNEWS, BoardType.TRENDING
	);

	private final BoardRepository boardRepository;
	private final BoardFavoriteFinder boardFavoriteFinder;

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
		return boardRepository.findBoardIdsByBoardTypeIn(DEFAULT_FAVORITE_BOARD_TYPES);
	}

	public Long findUniversityBoardId(Long universityId) {
		return boardRepository.findBoardIdByUniversityId(universityId)
			.orElse(null);
	}

	public BoardType findBoardType(Long boardId) {
		return BoardType.valueOf(boardRepository.findBoardTypeByBoardId(boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND)));
	}

	public List<Object[]> findAllBoardsWithPostCount() {
		List<Object[]> results = boardRepository.findAllBoardsWithPostCount();
		// 관리자용 제외 게시판 필터링
		return results.stream()
			.filter(arr -> !EXCLUDED_BOARD_TYPES_FOR_ADMIN.contains(((Board) arr[0]).getBoardType()))
			.toList();
	}

	public List<Object[]> findBoardsWithPostCount(BoardStatus status) {
		List<Object[]> results = boardRepository.findBoardsWithPostCount(status);
		// 관리자용 제외 게시판 필터링
		return results.stream()
			.filter(arr -> !EXCLUDED_BOARD_TYPES_FOR_ADMIN.contains(((Board) arr[0]).getBoardType()))
			.toList();
	}
}