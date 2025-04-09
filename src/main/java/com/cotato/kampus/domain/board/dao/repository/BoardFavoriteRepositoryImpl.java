package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardFavoriteEntity;
import com.cotato.kampus.domain.board.implement.port.BoardFavoriteRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardFavoriteRepositoryImpl implements BoardFavoriteRepository {

	private final BoardFavoriteJpaRepository boardFavoriteJpaRepository;

	@Override
	public List<BoardFavoriteEntity> findAllByUserId(Long userId) {
		return boardFavoriteJpaRepository.findAllByUserId(userId);
	}

	@Override
	public boolean existsByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteJpaRepository.existsByUserIdAndBoardId(userId, boardId);
	}

	@Override
	public Optional<BoardFavoriteEntity> findByUserIdAndBoardId(Long userId, Long boardId) {
		return boardFavoriteJpaRepository.findByUserIdAndBoardId(userId, boardId);
	}
}
