package com.cotato.kampus.domain.product.implement.productScrap;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.product.implement.port.ProductScrapRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductScrapFinder {

	private final ProductScrapRepository productScrapRepository;

	public boolean isAlreadyScrapped(Long productId, Long userId) {
		return productScrapRepository.existsByProductIdAndUserId(productId, userId);
	}
}
