package com.cotato.kampus.domain.product.api.response;

import com.cotato.kampus.domain.product.domain.ProductDetails;

public record ProductDetailResponse(
	ProductDetails productDetails
) {
	public static ProductDetailResponse from(ProductDetails productDetails) {
		return new ProductDetailResponse(productDetails);
	}
}
