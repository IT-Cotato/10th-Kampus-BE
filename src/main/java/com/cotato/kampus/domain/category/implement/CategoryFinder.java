package com.cotato.kampus.domain.category.implement;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.port.CategoryRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryFinder {

	private final CategoryRepository categoryRepository;

	public Category find(Long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	public Category find(String categoryName) {
		return categoryRepository.findByCategoryName(categoryName)
			.orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}
}
