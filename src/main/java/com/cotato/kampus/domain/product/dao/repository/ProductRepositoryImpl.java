package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductEntity;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;

	@Override
	public Product save(Product product) {
		ProductEntity entity = ProductEntity.fromDomain(product);
		return productJpaRepository.save(entity).toDomain();
	}
}
