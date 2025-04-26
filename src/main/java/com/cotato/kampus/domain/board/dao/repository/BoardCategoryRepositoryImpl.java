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
	public List<BoardCategory> saveAll(List<BoardCategory> boardCategories) {
		List<BoardCategoryEntity> boardCategoryEntities = boardCategories.stream()
			.map(BoardCategoryEntity::fromDomain)
			.toList();

		return boardCategoryJpaRepository.saveAll(boardCategoryEntities).stream()
			.map(BoardCategoryEntity::toDomain)
			.toList();
	}

	@Override
	public List<Long> findAllCategoryIdByBoardId(Long boardId) {
		return boardCategoryJpaRepository.findAllCategoryIdByBoardId(boardId);
	}

	@Override
	public boolean existsByCategoryIdAndBoardId(Long categoryId, Long boardId){
		return boardCategoryJpaRepository.existsByCategoryIdAndBoardId(categoryId, boardId);
	}
}
