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
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryAppender categoryAppender;
	private final UserValidator userValidator;
	private final CategoryFinder categoryFinder;

	@Transactional
	public Long createCategory(String categoryName) {
		// 1. 관리자 검증
		userValidator.validateAdminAccess();

		// 2. 카테고리 이름 중복 검증
		boolean isDuplicate = categoryFinder.existsByCategoryName(categoryName);
		if(isDuplicate) {
			throw new AppException(ErrorCode.CATEGORY_DUPLICATED);
		}

		// 3. 카테고리 생성
		return categoryAppender.append(categoryName).getId();
	}

	public List<Category> findAllCategory() {
		return categoryFinder.findAll();
	}
}
