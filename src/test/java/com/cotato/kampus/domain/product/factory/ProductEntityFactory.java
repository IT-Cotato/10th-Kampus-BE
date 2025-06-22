package com.cotato.kampus.domain.product.factory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import com.cotato.kampus.domain.product.dao.entity.ProductEntity;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.enums.ProductStatus;

public class ProductEntityFactory {

	public static ProductEntity create(String title, ProductStatus status, LocalDateTime createdTime) {
		Product product = Product.create(1L, title, 10000, "설명");

		if (status != ProductStatus.ACTIVE) {
			product = product.withProductStatus(status);
		}

		ProductEntity entity = ProductEntity.fromDomain(product);
		setCreatedTime(entity, createdTime);

		return entity;
	}

	public static ProductEntity create(String title, ProductStatus status) {
		return create(title, status, LocalDateTime.now());
	}

	private static void setCreatedTime(ProductEntity entity, LocalDateTime createdTime) {
		try {
			Field field = entity.getClass().getSuperclass().getDeclaredField("createdTime");
			field.setAccessible(true);
			field.set(entity, createdTime);
		} catch (Exception e) {
			throw new RuntimeException("Failed to set createdTime", e);
		}
	}
}