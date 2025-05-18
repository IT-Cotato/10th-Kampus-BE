package com.cotato.kampus.domain.product.implement.productScrap;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.product.domain.ProductScrap;
import com.cotato.kampus.domain.product.implement.port.ProductScrapRepository;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductScrapFinder {

	private final ProductScrapRepository productScrapRepository;

	public boolean isScrapped(Long productId, Long userId) {
		return productScrapRepository.existsByProductIdAndUserId(productId, userId);
	}

	public Slice<ProductScrap> getScrappedProducts(Long userId, int page, int size) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, size, Sort.Direction.DESC);
		return productScrapRepository.findAllByUserId(userId, customPageRequest.of("id"));
	}
}
