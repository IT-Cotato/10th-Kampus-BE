package com.cotato.kampus.domain.board.implement.boardFavorite;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteFinder {

	private final BoardFavoriteRepository boardFavoriteRepository;

	public List<Long> findFavoriteBoardIds(Long userId) {
		return boardFavoriteRepository.findAllByUserId(userId)
			.stream()
			.map(BoardFavorite::getBoardId)
			.toList();
	}

	public boolean existsByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteRepository.existsByUserIdAndBoardId(userId, boardId);
	}

	public BoardFavorite findByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteRepository.findByUserIdAndBoardId(userId, boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_FAVORITE_NOT_FOUND));
	}
}