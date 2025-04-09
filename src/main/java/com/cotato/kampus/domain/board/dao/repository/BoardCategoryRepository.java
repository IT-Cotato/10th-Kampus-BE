package com.cotato.kampus.domain.board.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategory;

@Repository
public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

	List<BoardCategory> findAllByBoardId(Long boardId);

	Optional<BoardCategory> findByBoardIdAndCategoryName(Long boardId, String categoryName);
}
