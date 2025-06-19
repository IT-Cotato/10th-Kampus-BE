package com.cotato.kampus.domain.category.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Category {

	private final Long id;
	private final String categoryName;

	@Builder
	public Category (Long id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
		validate();
	}

	private void validate() {
		if (categoryName == null) {
			throw new AppException(ErrorCode.TRENDING_POST_ID_REQUIRED);
		}
	}

	public Category withUpdateInfo(String categoryName) {
		return Category.builder()
			.id(this.id)
			.categoryName(categoryName)
			.build();
	}
}
