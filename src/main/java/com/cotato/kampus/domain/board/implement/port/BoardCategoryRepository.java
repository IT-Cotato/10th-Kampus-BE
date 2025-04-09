package com.cotato.kampus.domain.board.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.board.dao.entity.BoardCategoryEntity;
import com.cotato.kampus.domain.board.domain.BoardCategory;

public interface BoardCategoryRepository{

	List<BoardCategory> findAllByBoardId(Long boardId);

	Optional<BoardCategory> findByBoardIdAndCategoryName(Long boardId, String categoryName);

	List<BoardCategory> saveAll(List<BoardCategory> boardCategoryEntities);
}
