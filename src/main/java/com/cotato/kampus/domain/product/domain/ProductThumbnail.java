package com.cotato.kampus.domain.product.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record ProductThumbnail(
	Long productId,
	String title,
	int price,
	String photoUrl,
	ProductStatus productStatus,
	int scrapCount,
	int chatCount,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime,
	boolean isScrapped
) {
	public static ProductThumbnail from(Product product, String photoUrl, boolean isScrapped, int chatCount) {
		return new ProductThumbnail(
			product.getId(),
			product.getTitle(),
			product.getPrice(),
			photoUrl,
			product.getStatus(),
			product.getScrapCount(),
			chatCount,
			product.getCreatedTime(),
			isScrapped
		);
	}
}
