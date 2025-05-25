package com.cotato.kampus.domain.board.implement.boardFavorite;

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
public class BoardFavoriteManager {
	private final BoardFavoriteRepository boardFavoriteRepository;

	@Transactional
	public Long appendFavoriteBoard(Long userId, Long boardId) {
		// 즐겨찾기 여부 중복 체크
		boolean exists = boardFavoriteRepository.existsByUserIdAndBoardId(userId, boardId);
		if(exists){
			throw new AppException(ErrorCode.BOARD_ALREADY_FAVORITED);
		}

		// 즐겨찾기 추가
		BoardFavorite boardFavorite = BoardFavorite.builder()
			.boardId(boardId)
			.userId(userId)
			.build();

		return boardFavoriteRepository.save(boardFavorite).getBoardId();
	}

	@Transactional
	public void deleteFavoriteBoard(BoardFavorite boardFavorite) {
		boardFavoriteRepository.delete(boardFavorite);
	}
}
