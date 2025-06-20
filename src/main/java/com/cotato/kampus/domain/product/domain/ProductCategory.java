package com.cotato.kampus.domain.product.domain;

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
	}

	public ProductCategory withUpdateInfo(String categoryName) {
		return ProductCategory.builder()
			.id(this.id)
			.categoryName(categoryName)
			.build();
	}
}