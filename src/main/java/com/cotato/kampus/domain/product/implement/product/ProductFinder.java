package com.cotato.kampus.domain.product.implement.product;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductFinder {

	private final ProductRepository productRepository;

	public Product findById(Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
	}
}
