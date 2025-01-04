package com.cotato.kampus.domain.product.dto.response;

public record ProductCreateResponse(
	Long productId
) {
	public static ProductCreateResponse of(Long productId) {
		return new ProductCreateResponse(productId);
	}
}