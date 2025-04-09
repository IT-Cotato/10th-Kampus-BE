package com.cotato.kampus.domain.board.implement.boardFavorite;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteReader {

	private final BoardFavoriteRepository boardFavoriteRepository;

	public List<Long> findFavoriteBoardIds(Long userId) {
		return boardFavoriteRepository.findAllByUserId(userId)
			.stream()
			.map(BoardFavorite::getBoardId)
			.toList();
	}
}