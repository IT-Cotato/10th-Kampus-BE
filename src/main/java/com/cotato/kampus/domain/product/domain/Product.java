package com.cotato.kampus.domain.product.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {

	private final Long id;
	private final Long userId;
	private final String title;
	private final Integer price;
	private final String description;
	private final Integer viewCount;
	private final Integer scrapCount;
	private final Integer bumpCount;
	private final LocalDateTime bumpedTime;
	private final ProductStatus status;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	@Builder(access = AccessLevel.PRIVATE)
	private Product(Long id, Long userId, String title, Integer price, String description, Integer viewCount,
		Integer scrapCount, Integer bumpCount, LocalDateTime bumpedTime, ProductStatus status,
		LocalDateTime createdTime, LocalDateTime lastModifiedTime) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.description = description;
		this.viewCount = viewCount;
		this.scrapCount = scrapCount;
		this.bumpCount = bumpCount;
		this.bumpedTime = bumpedTime;
		this.status = status;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public static Product create(Long userId, String title, Integer price, String description) {
		return Product.builder()
			.userId(userId)
			.title(title)
			.price(price)
			.description(description)
			.viewCount(0)
			.scrapCount(0)
			.bumpCount(0)
			.bumpedTime(LocalDateTime.now())
			.status(ProductStatus.ACTIVE)
			.build();
	}

	// createdTime과 lastModifiedTime은 DB에서만 관리되므로, 도메인 생성(create)에선 제외되고, fromEntity에서만 주입됨
	public static Product fromEntity(
		Long id, Long userId, String title, Integer price, String description, Integer viewCount,
		Integer scrapCount, Integer bumpCount, LocalDateTime bumpedTime, ProductStatus status,
		LocalDateTime createdTime, LocalDateTime lastModifiedTime) {
		return Product.builder()
			.id(id)
			.userId(userId)
			.title(title)
			.price(price)
			.description(description)
			.viewCount(viewCount)
			.scrapCount(scrapCount)
			.bumpCount(bumpCount)
			.bumpedTime(bumpedTime)
			.status(status)
			.createdTime(createdTime)
			.lastModifiedTime(lastModifiedTime)
			.build();
	}

	public Product withUpdateInfo(String title, Integer price, String description) {
		return createCopy(title, price, description, this.viewCount, this.scrapCount,
			this.bumpCount, this.bumpedTime, this.status);
	}

	public Product withProductStatus(ProductStatus status) {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount,
			this.bumpCount, this.bumpedTime, status);
	}

	public Product increaseViewCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount + 1, this.scrapCount,
			this.bumpCount, this.bumpedTime, this.status);
	}

	public Product increaseScrapCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount + 1, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product decreaseScrapCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, Math.max(0, this.scrapCount - 1),
			this.bumpCount, this.bumpedTime, this.status);
	}

	// 끌어올리기 규칙 필요
	public Product bump() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount, this.bumpCount + 1,
			LocalDateTime.now(), this.status);
	}

	private Product createCopy(String title, Integer price, String description, Integer viewCount,
		Integer scrapCount, Integer bumpCount, LocalDateTime bumpedTime, ProductStatus status) {
		return Product.builder()
			.id(this.id)
			.userId(this.userId)
			.title(title)
			.price(price)
			.description(description)
			.viewCount(viewCount)
			.scrapCount(scrapCount)
			.bumpCount(bumpCount)
			.bumpedTime(bumpedTime)
			.status(status)
			.createdTime(createdTime)
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}

	public void validateEditable(Long userId) {
		validateOwner(userId);
		validateNotDeleted();
	}

	private void validateOwner(Long userId) {
		if (!userId.equals(this.userId)) {
			throw new AppException(ErrorCode.FORBIDDEN_PRODUCT_EDIT);
		}
	}

	public void validateNotDeleted() {
		if (this.status.equals(ProductStatus.DELETED)) {
			throw new AppException(ErrorCode.ALREADY_DELETED_PRODUCT);
		}
	}
}