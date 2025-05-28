package com.cotato.kampus.domain.product.domain;

public record ProductCategoryInfo(
	Long productCategoryId,
	String categoryName
) {
	public static ProductCategoryInfo from(ProductCategory productCategory) {
		return new ProductCategoryInfo(
			productCategory.getId(),
			productCategory.getCategoryName()
		);
	}
}
