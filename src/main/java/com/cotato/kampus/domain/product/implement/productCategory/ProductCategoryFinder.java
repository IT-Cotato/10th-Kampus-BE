package com.cotato.kampus.domain.product.implement.productCategory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductCategoryFinder {

	private final ProductCategoryRepository productCategoryRepository;

	public ProductCategory find(String categoryName) {
		return productCategoryRepository.findByCategoryName(categoryName)
			.orElseThrow(() -> new AppException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));
	}

	public boolean existsByCategoryName(String categoryName) {
		return productCategoryRepository.existsByCategoryName(categoryName);
	}
}
