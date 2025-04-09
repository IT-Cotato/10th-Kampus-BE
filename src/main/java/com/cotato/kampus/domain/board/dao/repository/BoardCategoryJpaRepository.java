package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

public interface BoardCategoryJpaRepository extends JpaRepository<BoardCategoryEntity, Long> {

	List<BoardCategoryEntity> findAllByBoardId(Long boardId);

	Optional<BoardCategoryEntity> findByBoardIdAndCategoryName(Long boardId, String categoryName);

	List<BoardCategoryEntity> saveAll(List<BoardCategoryEntity> boardCategoryEntities);

}
