package com.cotato.kampus.domain.product.dao.entity;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "price", nullable = false)
	private Integer price;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
	private Integer viewCount = 0;

	@Column(name = "scrap_count", nullable = false, columnDefinition = "int default 0")
	private Integer scrapCount = 0;

	@Column(name = "chat_count", nullable = false, columnDefinition = "int default 0")
	private Integer chatCount = 0;

	@Column(name = "bumped_count", nullable = false, columnDefinition = "int default 0")
	private Integer bumpCount = 0;

	@Column(name = "bumped_time", nullable = false)
	private LocalDateTime bumpedTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_status", nullable = false)
	private ProductStatus productStatus;

	public Product toDomain() {
		return Product.builder()
			.id(id)
			.userId(userId)
			.title(title)
			.price(price)
			.description(description)
			.viewCount(viewCount)
			.scrapCount(scrapCount)
			.chatCount(chatCount)
			.bumpCount(bumpCount)
			.bumpedTime(bumpedTime)
			.status(productStatus)
			.createdTime(getCreatedTime())
			.lastModifiedTime(getLastModifiedTime())
			.build();
	}

	public static ProductEntity fromDomain(Product product) {
		ProductEntity entity = new ProductEntity();
		entity.id = product.getId();
		entity.userId = product.getUserId();
		entity.title = product.getTitle();
		entity.price = product.getPrice();
		entity.description = product.getDescription();
		entity.viewCount = product.getViewCount();
		entity.scrapCount = product.getScrapCount();
		entity.chatCount = product.getChatCount();
		entity.bumpCount = product.getBumpCount();
		entity.bumpedTime = product.getBumpedTime();
		entity.productStatus = product.getStatus();
		return entity;
	}
}
