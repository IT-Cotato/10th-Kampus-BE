package com.cotato.kampus.domain.product.implement.product;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.enums.ProductSortType;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
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

	public Slice<Product> findAll(int page, int size, ProductSortType sortType) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, size, sortType.getDirection());
		return productRepository.findAllByProductStatusNot(ProductStatus.DELETED, customPageRequest.of(
			sortType.getProperty()));
	}

	public Slice<Product> findAllByProductIds(List<Long> productIds, int page, int size, ProductSortType sortType) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, size, sortType.getDirection());
		return productRepository.findAllByIdInAndProductStatusNot(productIds, ProductStatus.DELETED, customPageRequest.of(sortType.getProperty()));
	}

	public List<Product> findAllByProductIds(List<Long> productIds) {
		return productRepository.findAllByIdInAndProductStatusNot(productIds, ProductStatus.DELETED);
	}
}
