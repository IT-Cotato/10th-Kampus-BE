package com.cotato.kampus.domain.product.dao.entity;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category_mapping")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductCategoryMappingEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "product_category_mapping_id")
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "product_category_id", nullable = false)
	private Long categoryId;

	public static ProductCategoryMappingEntity fromDomain(ProductCategoryMapping productCategoryMapping) {
		ProductCategoryMappingEntity result = new ProductCategoryMappingEntity();
		result.id = productCategoryMapping.getId();
		result.productId = productCategoryMapping.getProductId();
		result.categoryId = productCategoryMapping.getCategoryId();
		return result;
	}

	public ProductCategoryMapping toDomain() {
		return ProductCategoryMapping.builder()
			.id(id)
			.productId(productId)
			.categoryId(categoryId)
			.build();
	}
}
