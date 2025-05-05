package com.cotato.kampus.domain.product.dao.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductCategoryEntity;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

	private final ProductCategoryJpaRepository productCategoryJpaRepository;

	@Override
	public Optional<ProductCategory> findByCategoryName(String categoryName) {
		return productCategoryJpaRepository.findByCategoryName(categoryName)
			.map(ProductCategoryEntity::toDomain);
	}
}
