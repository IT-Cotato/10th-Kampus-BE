package com.cotato.kampus.domain.category.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.CategoryAppender;
import com.cotato.kampus.domain.category.implement.CategoryFinder;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final ApiUserResolver apiUserResolver;
	private final CategoryAppender categoryAppender;
	private final UserValidator userValidator;
	private final CategoryFinder categoryFinder;

	@Transactional
	public Long createCategory(String categoryName) {
		UserDto user = apiUserResolver.getCurrentUserDto();
		userValidator.validateAdminAccess(user);

		return categoryAppender.append(categoryName).getId();
	}

	public List<Category> findAllCategory() {
		return categoryFinder.findAll();
	}
}
