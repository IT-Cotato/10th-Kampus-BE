package com.cotato.kampus.domain.product.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.product.domain.ProductCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_id")
	private Long id;

	@Column(name = "category_name", nullable = false, unique = true)
	private String categoryName;

	public static ProductCategoryEntity fromDomain(ProductCategory category) {
		ProductCategoryEntity result = new ProductCategoryEntity();
		result.id = category.getId();
		result.categoryName = category.getCategoryName();
		return result;
	}

	public ProductCategory toDomain() {
		return ProductCategory.builder()
			.id(id)
			.categoryName(categoryName)
			.build();
	}
}
