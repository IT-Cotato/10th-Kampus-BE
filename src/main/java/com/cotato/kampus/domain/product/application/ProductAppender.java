package com.cotato.kampus.domain.product.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.ProductCategory;
import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.dao.ProductRepository;
import com.cotato.kampus.domain.product.domain.Product;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAppender {

	private final ProductRepository productRepository;
	private final ApiUserResolver apiUserResolver;

	@Transactional
	public Long append(String title, Long sellPrice, String description, ProductCategory productCategory) {

		Long userId = apiUserResolver.getCurrentUserId();

		Product product = Product.builder()
			.title(title)
			.userId(userId)
			.sellPrice(sellPrice)
			.description(description)
			.productCategory(productCategory)
			.productStatus(ProductStatus.ACTIVE)
			.chats(0L)
			.views(0L)
			.scraps(0L)
			.build();

		return productRepository.save(product).getId();
	}
}
