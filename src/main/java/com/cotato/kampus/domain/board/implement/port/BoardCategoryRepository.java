package com.cotato.kampus.domain.board.implement.port;

import java.util.List;
import com.cotato.kampus.domain.board.domain.BoardCategory;

public interface BoardCategoryRepository{

	List<BoardCategory> findAllByBoardId(Long boardId);

	List<BoardCategory> saveAll(List<BoardCategory> boardCategoryEntities);

	List<Long> findAllCategoryIdByBoardId(Long boardId);

	boolean existsByCategoryIdAndBoardId(Long categoryId, Long boardId);
}
