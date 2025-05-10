package com.cotato.kampus.domain.product.implement.productCategory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class ProductCategoryManager {

	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategory append(String categoryName) {
		ProductCategory category = ProductCategory.builder()
			.categoryName(categoryName)
			.build();

		return productCategoryRepository.save(category);
	}
}
