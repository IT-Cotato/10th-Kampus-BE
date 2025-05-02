package com.cotato.kampus.domain.product.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

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
	private final Integer chatCount;
	private final Integer bumpCount;
	private final LocalDateTime bumpedTime;
	private final ProductStatus status;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	@Builder
	public Product(
		Long id,
		Long userId,
		String title,
		Integer price,
		String description,
		Integer viewCount,
		Integer scrapCount,
		Integer chatCount,
		Integer bumpCount,
		LocalDateTime bumpedTime,
		ProductStatus status,
		LocalDateTime createdTime,
		LocalDateTime lastModifiedTime
	) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.price = price;
		this.description = description;
		this.viewCount = viewCount != null ? viewCount : 0;
		this.scrapCount = scrapCount != null ? scrapCount : 0;
		this.chatCount = chatCount != null ? chatCount : 0;
		this.bumpCount = bumpCount != null ? bumpCount : 0;
		this.bumpedTime = bumpedTime != null ? bumpedTime : LocalDateTime.now();
		this.status = status;
		this.createdTime = createdTime;
		this.lastModifiedTime = lastModifiedTime;
		validate();
	}

	private void validate() {
		validateUserId();
		validateTitle();
		validatePrice();
		validateDescription();
		validateStatus();
	}

	private void validateUserId() {
		if (userId == null) {
			throw new AppException(ErrorCode.PRODUCT_USER_ID_REQUIRED);
		}
	}

	private void validateTitle() {
		if (title == null || title.trim().isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_TITLE_REQUIRED);
		}
	}

	private void validatePrice() {
		if (price == null) {
			throw new AppException(ErrorCode.PRODUCT_PRICE_REQUIRED);
		}
	}

	private void validateDescription() {
		if (description == null || description.trim().isEmpty()) {
			throw new AppException(ErrorCode.PRODUCT_DESCRIPTION_REQUIRED);
		}
	}

	private void validateStatus() {
		if (status == null) {
			throw new AppException(ErrorCode.PRODUCT_STATUS_REQUIRED);
		}
	}

	private Product createCopy(
		String title,
		Integer price,
		String description,
		Integer viewCount,
		Integer scrapCount,
		Integer chatCount,
		Integer bumpCount,
		LocalDateTime bumpedTime,
		ProductStatus status
	) {
		return Product.builder()
			.id(getId())
			.userId(getUserId())
			.title(title)
			.price(price)
			.description(description)
			.viewCount(viewCount)
			.scrapCount(scrapCount)
			.chatCount(chatCount)
			.bumpCount(bumpCount)
			.bumpedTime(bumpedTime)
			.status(status)
			.createdTime(getCreatedTime())
			.lastModifiedTime(LocalDateTime.now())
			.build();
	}

	public Product withUpdateInfo(String title, Integer price, String description){
		return createCopy(title, price, description, this.viewCount, this.scrapCount, this.chatCount, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product withProductStatus(ProductStatus status) {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount, this.chatCount, this.bumpCount,
			this.bumpedTime, status);
	}

	public Product increaseViewCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount + 1, this.scrapCount, this.chatCount, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product increaseScrapCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount + 1, this.chatCount, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product decreaseScrapCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, Math.max(0, this.scrapCount - 1), this.chatCount, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product increaseChatCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount, this.chatCount + 1, this.bumpCount,
			this.bumpedTime, this.status);
	}

	public Product decreaseChatCount() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount, Math.max(0, this.chatCount - 1), this.bumpCount,
			this.bumpedTime, this.status);
	}

	// 끌어올리기 규칙 필요
	public Product bump() {
		return createCopy(this.title, this.price, this.description, this.viewCount, this.scrapCount, this.chatCount, this.bumpCount + 1,
			LocalDateTime.now(), this.status);
	}
}