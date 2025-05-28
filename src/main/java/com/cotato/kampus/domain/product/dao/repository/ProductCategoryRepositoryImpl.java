package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;
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

	@Override
	public ProductCategory save(ProductCategory productCategory) {
		ProductCategoryEntity entity = ProductCategoryEntity.fromDomain(productCategory);
		return productCategoryJpaRepository.save(entity).toDomain();
	}

	@Override
	public boolean existsByCategoryName(String categoryName) {
		return productCategoryJpaRepository.existsByCategoryName(categoryName);
	}

	@Override
	public List<ProductCategory> findAllByIdIn(List<Long> categoryIds) {
		return productCategoryJpaRepository.findAllByIdIn(categoryIds).stream()
			.map(ProductCategoryEntity::toDomain)
			.toList();
	}

	@Override
	public List<ProductCategory> findAll() {
		return productCategoryJpaRepository.findAll().stream()
			.map(ProductCategoryEntity::toDomain)
			.toList();
	}
}
