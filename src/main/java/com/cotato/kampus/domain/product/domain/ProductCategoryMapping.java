package com.cotato.kampus.domain.product.domain;

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
	}
}