package com.cotato.kampus.domain.board.implement.boardFavorite;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteManager {
	private final BoardFavoriteRepository boardFavoriteRepository;

	@Transactional
	public Long appendFavoriteBoard(Long userId, Long boardId) {
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
