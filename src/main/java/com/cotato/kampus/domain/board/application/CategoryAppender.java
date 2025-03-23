package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.CategoryRepository;
import com.cotato.kampus.domain.board.domain.BoardCategory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryAppender {

	private final CategoryRepository categoryRepository;

	@Transactional
	public void appendCategories(Long boardId, List<String> categories) {
		List<BoardCategory> boardCategoryList = categories.stream()
			.map(categoryName -> BoardCategory.builder()
				.categoryName(categoryName)
				.boardId(boardId)
				.build())
			.toList();

		categoryRepository.saveAll(boardCategoryList);
	}
}
