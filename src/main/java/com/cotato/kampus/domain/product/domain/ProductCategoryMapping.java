package com.cotato.kampus.domain.product.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCategoryMapping {

	private final Long id;
	private final Long productId;
	private final Long categoryId;

	@Builder
	public ProductCategoryMapping(Long id, Long productId, Long categoryId) {
		this.id = id;
		this.productId = productId;
		this.categoryId = categoryId;
		validate();
	}

	public void validate() {
		if (productId == null) {
			throw new AppException(ErrorCode.PRODUCT_CATEGORY_PRODUCT_ID_REQUIRED);
		}

		if (categoryId == null) {
			throw new AppException(ErrorCode.PRODUCT_CATEGORY_CATEGORY_ID_REQUIRED);
		}
	}
}