package com.cotato.kampus.domain.board.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;

@Repository
public interface BoardCategoryRepository extends JpaRepository<BoardCategoryEntity, Long> {

	List<BoardCategoryEntity> findAllByBoardId(Long boardId);

	Optional<BoardCategoryEntity> findByBoardIdAndCategoryName(Long boardId, String categoryName);
}
