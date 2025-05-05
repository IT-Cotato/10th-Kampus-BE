package com.cotato.kampus.domain.product.api.response;

public record ProductCreateResponse(
	Long productId
) {
	public static ProductCreateResponse of(Long productId) {
		return new ProductCreateResponse(productId);
	}
}