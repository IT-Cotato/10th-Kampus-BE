package com.cotato.kampus.domain.product.api.response;

import java.util.List;

import com.cotato.kampus.domain.product.domain.ProductCategoryInfo;

public record ProductCategoryFindResponse(
	List<ProductCategoryInfo> categoryInfos
) {
	public static ProductCategoryFindResponse from(List<ProductCategoryInfo> categoryInfos) {
		return new ProductCategoryFindResponse(categoryInfos);
	}
}
