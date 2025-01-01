package com.cotato.kampus.domain.product.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;
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

	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private ProductCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_status")
	private ProductStatus productStatus;

	@Column(name = "title")
	private String title;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "scraps")
	private Long scraps;

	@Column(name = "chats")
	private Long chats;

	@Column(name = "sell_price")
	private Long sellPrice;

	@Column(name = "views")
	private Long views;

	@Builder
	public Product(Long userId, ProductCategory category, ProductStatus productStatus,
		String title, String description, Long scraps, Long chats,
		Long sellPrice, Long views) {
		this.userId = userId;
		this.category = category;
		this.productStatus = productStatus;
		this.title = title;
		this.description = description;
		this.scraps = scraps;
		this.chats = chats;
		this.sellPrice = sellPrice;
		this.views = views;
	}
}
