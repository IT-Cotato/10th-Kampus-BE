package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;
import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.board.implement.port.BoardCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardCategoryRepositoryImpl implements BoardCategoryRepository {

	private final BoardCategoryJpaRepository boardCategoryJpaRepository;

	@Override
	public List<BoardCategory> findAllByBoardId(Long boardId) {
		return boardCategoryJpaRepository.findAllByBoardId(boardId).stream()
			.map(BoardCategoryEntity::toDomain)
			.toList();
	}

	@Override
	public Optional<BoardCategory> findByBoardIdAndCategoryName(Long boardId, String categoryName) {
		return boardCategoryJpaRepository.findByBoardIdAndCategoryName(boardId, categoryName)
			.map(BoardCategoryEntity::toDomain);
	}

	@Override
	public List<BoardCategory> saveAll(List<BoardCategory> boardCategories) {
		List<BoardCategoryEntity> boardCategoryEntities = boardCategories.stream()
			.map(BoardCategoryEntity::fromDomain)
			.toList();

		return boardCategoryJpaRepository.saveAll(boardCategoryEntities).stream()
			.map(BoardCategoryEntity::toDomain)
			.toList();
	}
}
