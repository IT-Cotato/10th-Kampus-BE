package com.cotato.kampus.domain.product.implement.productCategory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryMappingFinder {

	private final ProductCategoryMappingRepository productCategoryMappingRepository;

	public List<Long> getIdsByCategory(Long categoryId) {
		return productCategoryMappingRepository.findAllProductIdsByCategoryId(categoryId);
	}
}
