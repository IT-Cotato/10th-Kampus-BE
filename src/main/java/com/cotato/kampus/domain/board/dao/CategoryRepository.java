package com.cotato.kampus.domain.board.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.domain.BoardCategory;

@Repository
public interface CategoryRepository extends JpaRepository<BoardCategory, Long> {

	List<BoardCategory> findAllByBoardId(Long boardId);

	Optional<Category> findByBoardIdAndCategoryName(Long boardId, String categoryName);
}
