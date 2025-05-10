package com.cotato.kampus.domain.product.api.response;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.domain.ProductDetails;
import com.cotato.kampus.domain.product.domain.ProductPhotoInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ProductDetailResponse(
	Long productId,
	Long sellerId,
	String sellerName,
	String title,
	int price,
	String description,
	List<ProductPhotoInfo> photos,
	ProductStatus productStatus,
	int scrapCount,
	int chatCount,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isScrapped
) {
	public static ProductDetailResponse from(ProductDetails productDetails) {
		return new ProductDetailResponse(
			productDetails.productId(),
			productDetails.sellerId(),
			productDetails.sellerName(),
			productDetails.title(),
			productDetails.price(),
			productDetails.description(),
			productDetails.photos(),
			productDetails.postStatus(),
			productDetails.scrapCount(),
			productDetails.chatCount(),
			productDetails.createdTime(),
			productDetails.isAuthor(),
			productDetails.isScrapped()
		);
	}
}
