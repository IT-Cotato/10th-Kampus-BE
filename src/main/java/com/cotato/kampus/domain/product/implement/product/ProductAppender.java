package com.cotato.kampus.domain.product.implement.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAppender {

	private final ProductRepository productRepository;

	@Transactional
	public Product append(Long userId, String title, Integer price, String description) {

		Product product = Product.builder()
			.userId(userId)
			.title(title)
			.price(price)
			.description(description)
			.status(ProductStatus.ACTIVE)
			.build();
		return productRepository.save(product);
	}
}
