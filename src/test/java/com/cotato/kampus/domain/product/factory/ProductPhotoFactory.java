package com.cotato.kampus.domain.product.factory;

import com.cotato.kampus.domain.product.domain.ProductPhoto;

public class ProductPhotoFactory {

	public static ProductPhoto createProductPhoto(Long id, Long productId, String photoUrl, Integer order) {
		return ProductPhoto.builder()
			.id(id)
			.productId(productId)
			.photoUrl(photoUrl)
			.order(order)
			.build();
	}
}
