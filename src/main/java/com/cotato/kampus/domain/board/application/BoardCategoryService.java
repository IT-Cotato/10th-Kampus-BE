package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryFinder;
import com.cotato.kampus.domain.board.implement.board.BoardFinder;
import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryFinder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardCategoryService {

	private final BoardFinder boardFinder;
	private final BoardCategoryFinder boardCategoryFinder;
	private final CategoryFinder categoryFinder;

	public List<Category> findCategories(Long boardId) {
		// 존재하는 게시판인지 확인
		boardFinder.findBoard(boardId);

		// 카테고리 조회
		List<Long> categoryIds = boardCategoryFinder.findAllByBoardId(boardId).stream()
			.map(BoardCategory::getCategoryId).toList();

		return categoryIds.stream().map(categoryFinder::find).toList();
	}
}
