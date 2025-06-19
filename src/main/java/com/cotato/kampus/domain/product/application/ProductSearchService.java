package com.cotato.kampus.domain.product.application;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.implement.product.ProductDtoMapper;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

	private final ProductFinder productFinder;
	private final ProductDtoMapper productDtoMapper;
	private final ApiUserResolver apiUserResolver;

	public Slice<ProductThumbnail> searchProducts(String keyword, int page) {
		Long currentUserId = apiUserResolver.getCurrentUserId();
		Slice<Product> products = productFinder.searchProducts(keyword, page);

		return productDtoMapper.toProductThumbnails(products, currentUserId);
	}
}
