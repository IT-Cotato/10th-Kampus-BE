package com.cotato.kampus.domain.product.domain;

public record ProductPhotoInfo(
	int order,
	String photoUrl
) {
	public static ProductPhotoInfo from(ProductPhoto productPhoto) {
		return new ProductPhotoInfo(
			productPhoto.getOrder(),
			productPhoto.getPhotoUrl());
	}
}
