package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

public interface BoardCategoryJpaRepository extends JpaRepository<BoardCategoryEntity, Long> {

	List<BoardCategoryEntity> findAllByBoardId(Long boardId);

	@Query("SELECT b.categoryId FROM BoardCategoryEntity b WHERE b.boardId = :boardId")
	List<Long> findAllCategoryIdByBoardId(@Param("boardId") Long boardId);

	boolean existsByCategoryIdAndBoardId(Long categoryId, Long boadId);
}
