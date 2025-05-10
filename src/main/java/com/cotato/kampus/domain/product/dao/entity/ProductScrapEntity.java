package com.cotato.kampus.domain.product.dao.entity;

import com.cotato.kampus.domain.product.domain.ProductScrap;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_scrap")
@Getter
@NoArgsConstructor
public class ProductScrapEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	public static ProductScrapEntity fromDomain(ProductScrap productScrap) {
		ProductScrapEntity entity = new ProductScrapEntity();
		entity.id = productScrap.getId();
		entity.productId = productScrap.getProductId();
		entity.userId = productScrap.getUserId();
		return entity;
	}
}
