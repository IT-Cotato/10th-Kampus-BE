package com.cotato.kampus.domain.category.implement;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.port.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class CategoryManager {

	private final CategoryRepository categoryRepository;

	public Category append(String categoryName){
		Category category = Category.builder()
			.categoryName(categoryName)
			.build();

		return categoryRepository.save(category);
	}

	public Category update(Category category) {
		return categoryRepository.save(category);
	}
}
