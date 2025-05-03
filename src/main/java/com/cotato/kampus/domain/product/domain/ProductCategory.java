package com.cotato.kampus.domain.product.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCategory {

	private final Long id;
	private final String categoryName;

	@Builder
	public ProductCategory (Long id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
		validate();
	}

	private void validate() {
		if(categoryName == null) {
			throw new AppException(ErrorCode.CATEGORY_NAME_REQUIRED);
		}
	}
}
