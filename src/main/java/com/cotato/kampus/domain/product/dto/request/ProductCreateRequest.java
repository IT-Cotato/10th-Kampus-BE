package com.cotato.kampus.domain.product.dto.request;

import com.cotato.kampus.domain.product.ProductCategory;

public record ProductCreateRequest(
	String title,
	Long sellPrice,
	String description,
	ProductCategory productCategory
	) {
}
