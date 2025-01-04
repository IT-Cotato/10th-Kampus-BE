package com.cotato.kampus.domain.product.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.product.ProductCategory;
import com.cotato.kampus.domain.product.ProductStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false)
	private ProductCategory productCategory;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_status", nullable = false)
	private ProductStatus productStatus;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@Column(name = "scraps", nullable = false)
	private Long scraps = 0L;

	@Column(name = "chats", nullable = false)
	private Long chats = 0L;

	@Column(name = "sell_price", nullable = false)
	private Long sellPrice;

	@Column(name = "views", nullable = false)
	private Long views = 0L;

	@Builder
	public Product(Long userId, ProductCategory productCategory, ProductStatus productStatus,
		String title, String description, Long scraps, Long chats,
		Long sellPrice, Long views) {
		this.userId = userId;
		this.productCategory = productCategory != null ? productCategory : ProductCategory.ETC;
		this.productStatus = productStatus != null ? productStatus : ProductStatus.ACTIVE;
		this.title = title;
		this.description = description;
		this.scraps = scraps != null ? scraps : 0L;
		this.chats = chats != null ? chats : 0L;
		this.sellPrice = sellPrice;
		this.views = views != null ? views : 0L;
	}
}
