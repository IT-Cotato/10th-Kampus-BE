package com.cotato.kampus.domain.product.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.cotato.kampus.domain.product.enums.ProductStatus;

public record ProductDetails(
	Long productId,
	Long sellerId,
	String sellerName,
	String title,
	int price,
	List<String> categories,
	String description,
	List<ProductPhotoInfo> photos,
	int viewCount,
	int scrapCount,
	int chatCount,
	int bumpCount,
	ProductStatus postStatus,
	LocalDateTime bumpedTime,
	LocalDateTime createdTime,
	boolean isAuthor,
	boolean isScrapped
) {
	public static ProductDetails of(
		Product product,
		String sellerName,
		List<ProductPhoto> productPhotos,
		boolean isAuthor,
		boolean isScrapped,
		int chatCount,
		List<String> categories
	) {
		return new ProductDetails(
			product.getId(),
			product.getUserId(),
			sellerName,
			product.getTitle(),
			product.getPrice(),
			categories,
			product.getDescription(),
			productPhotos.stream()
					.map(ProductPhotoInfo::from)
					.toList(),
			product.getViewCount(),
			product.getScrapCount(),
			chatCount,
			product.getBumpCount(),
			product.getStatus(),
			product.getBumpedTime(),
			product.getCreatedTime(),
			isAuthor,
			isScrapped
		);
	}
}
