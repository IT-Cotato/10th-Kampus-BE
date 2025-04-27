package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

public interface BoardCategoryJpaRepository extends JpaRepository<BoardCategoryEntity, Long> {

	List<BoardCategoryEntity> findAllByBoardId(Long boardId);

	List<Long> findAllCategoryIdByBoardId(Long boardId);

	boolean existsByCategoryIdAndBoardId(Long categoryId, Long boadId);
}
