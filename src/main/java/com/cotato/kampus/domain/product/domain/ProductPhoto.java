package com.cotato.kampus.domain.product.domain;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

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
		validate();
	}

	private void validate() {
		if(productId == null) {
			throw new AppException(ErrorCode.PRODUCT_PHOTO_PRODUCT_ID_REQUIRED);
		}
		if(photoUrl == null) {
			throw new AppException(ErrorCode.PRUDUCT_PHOTO_URL_REQUIRED);
		}
	}
}
