package com.cotato.kampus.domain.product.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductPhoto {

	private final Long id;
	private final Long productId;
	private final String photoUrl;
	private final Integer order;

	@Builder
	public ProductPhoto(Long id, Long productId, String photoUrl, Integer order) {
		this.id = id;
		this.productId = productId;
		this.photoUrl = photoUrl;
		this.order = order;
	}
}
