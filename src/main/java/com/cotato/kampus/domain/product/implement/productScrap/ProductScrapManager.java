package com.cotato.kampus.domain.product.implement.productScrap;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductScrap;
import com.cotato.kampus.domain.product.implement.port.ProductScrapRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductScrapManager {

	private final ProductScrapRepository productScrapRepository;

	@Transactional
	public void append(Long productId, Long userId) {
		ProductScrap productScrap = ProductScrap.builder()
			.productId(productId)
			.userId(userId)
			.build();

		productScrapRepository.save(productScrap);
	}

	@Transactional
	public void delete(Long productId, Long userId) {
		productScrapRepository.deleteByProductIdAndUserId(productId, userId);
	}
}
