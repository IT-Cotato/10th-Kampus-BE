package com.cotato.kampus.domain.board.application;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.BoardFavoriteRepository;
import com.cotato.kampus.domain.board.domain.BoardFavorite;
import com.cotato.kampus.domain.common.application.ApiUserResolver;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFavoriteReader {

	private final BoardFavoriteRepository boardFavoriteRepository;

	@Transactional
	public Set<Long> findFavoriteBoardIds(Long userId) {
		return boardFavoriteRepository.findAllByUserId(userId)
			.stream()
			.map(BoardFavorite::getBoardId)
			.collect(Collectors.toSet());
	}
}