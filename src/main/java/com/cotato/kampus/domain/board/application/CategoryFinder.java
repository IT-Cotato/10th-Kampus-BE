package com.cotato.kampus.domain.board.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.board.dao.CategoryRepository;
import com.cotato.kampus.domain.board.domain.BoardCategory;
import com.cotato.kampus.domain.board.dto.CategoryDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryFinder {

	private final CategoryRepository categoryRepository;

	public List<CategoryDto> findCategories(Long boardId) {

		List<BoardCategory> boardCategoryList = categoryRepository.findAllByBoardId(boardId);

		return boardCategoryList.stream()
			.map(CategoryDto::from)
			.toList();
	}

	public CategoryDto findDto(Long boardId, String categoryName) {
		Category category = categoryRepository.findByBoardIdAndCategoryName(boardId, categoryName)
			.orElseThrow(() -> new AppException(ErrorCode.INVALID_CATEGORY));

		return CategoryDto.from(category);
	}
}
