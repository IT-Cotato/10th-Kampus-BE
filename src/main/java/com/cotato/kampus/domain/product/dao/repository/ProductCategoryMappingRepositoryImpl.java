package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductCategoryMappingEntity;
import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductCategoryMappingRepositoryImpl implements ProductCategoryMappingRepository {

	private final ProductCategoryMappingJpaRepository productCategoryMappingJpaRepository;

	@Override
	public ProductCategoryMapping save(ProductCategoryMapping productCategoryMapping) {
		ProductCategoryMappingEntity entity = ProductCategoryMappingEntity.fromDomain(productCategoryMapping);
		return productCategoryMappingJpaRepository.save(entity).toDomain();
	}

	@Override
	public List<Long> findAllProductIdsByCategoryId(Long productCategoryId) {
		return productCategoryMappingJpaRepository.findAllProductIdsByCategoryId(productCategoryId);
	}

	@Override
	public void deleteAllByProductId(Long productId) {
		productCategoryMappingJpaRepository.deleteAllByProductId(productId);
	}

	@Override
	public List<Long> findAllCategoryIdByProductId(Long productId) {
		return productCategoryMappingJpaRepository.findAllCategoryIdByProductId(productId);
	}
}
