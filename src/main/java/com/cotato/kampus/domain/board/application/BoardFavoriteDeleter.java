package com.cotato.kampus.domain.board.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardFavoriteRepository;
import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteDeleter {
	private final BoardFavoriteRepository boardFavoriteRepository;
	private final ApiUserResolver apiUserResolver;

	@Transactional
	public void deleteFavoriteBoard(Long boardId) {
		// 즐겨찾기 여부 체크
		BoardFavorite boardFavorite = boardFavoriteRepository.findByUserIdAndBoardId(apiUserResolver.getUserId(), boardId)
			.orElseThrow(() -> new AppException(ErrorCode.BOARD_FAVORITE_NOT_FOUND));

		boardFavoriteRepository.delete(boardFavorite);
	}
}
