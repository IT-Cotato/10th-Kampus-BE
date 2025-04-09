package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;
import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardCategoryRepositoryImpl implements BoardCategoryRepository {

	private final BoardCategoryJpaRepository boardCategoryJpaRepository;

	@Override
	public List<BoardCategoryEntity> findAllByBoardId(Long boardId) {
		return boardCategoryJpaRepository.findAllByBoardId(boardId);
	}

	@Override
	public Optional<BoardCategoryEntity> findByBoardIdAndCategoryName(Long boardId, String categoryName) {
		return boardCategoryJpaRepository.findByBoardIdAndCategoryName(boardId, categoryName);
	}
}
