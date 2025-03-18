package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.CategoryRepository;
import com.cotato.kampus.domain.board.domain.Category;
import com.cotato.kampus.domain.board.dto.CategoryDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryFinder {

	private final CategoryRepository categoryRepository;
	private final BoardFinder boardFinder;

	public List<CategoryDto> findCategories(Long boardId) {

		List<Category> categoryList = categoryRepository.findAllByBoardId(boardId);

		return categoryList.stream()
			.map(CategoryDto::from)
			.toList();
	}
}
