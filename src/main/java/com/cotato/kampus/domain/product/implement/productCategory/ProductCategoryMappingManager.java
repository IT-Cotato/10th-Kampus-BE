package com.cotato.kampus.domain.product.implement.productCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryMappingManager {
	private final ProductCategoryMappingRepository productCategoryMappingRepository;

	@Transactional
	public void saveAll(Long productId, List<Long> categoryIds) {
		categoryIds.forEach(categoryId -> {
			ProductCategoryMapping productCategoryMapping = ProductCategoryMapping.builder()
				.productId(productId)
				.categoryId(categoryId)
				.build();
			productCategoryMappingRepository.save(productCategoryMapping);
		});
	}
}
