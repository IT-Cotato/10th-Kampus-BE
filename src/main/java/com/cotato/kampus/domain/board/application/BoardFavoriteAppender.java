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
public class BoardFavoriteAppender {
	private final BoardFavoriteRepository boardFavoriteRepository;
	private final ApiUserResolver apiUserResolver;

	@Transactional
	public Long appendFavoriteBoard(Long boardId) {
		// 즐겨찾기 여부 중복 체크
		boolean exists = boardFavoriteRepository.existsByUserIdAndBoardId(apiUserResolver.getUserId(), boardId);
		if(exists){
			throw new AppException(ErrorCode.BOARD_ALREADY_FAVORITED);
		}

		// 즐겨찾기 추가
		BoardFavorite boardFavorite = BoardFavorite.builder()
			.boardId(boardId)
			.userId(apiUserResolver.getUserId())
			.build();

		return boardFavoriteRepository.save(boardFavorite).getBoardId();
	}
}
