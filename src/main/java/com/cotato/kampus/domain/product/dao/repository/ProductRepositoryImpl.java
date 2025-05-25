package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.enums.ProductStatus;
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

	@Override
	public Optional<Product> findById(Long productId) {
		return productJpaRepository.findById(productId)
			.map(ProductEntity::toDomain);
	}

	@Override
	public Slice<Product> findAllByProductStatusNot(ProductStatus status, Pageable pageable) {
		return productJpaRepository.findAllByProductStatusNot(status, pageable)
			.map(ProductEntity::toDomain);
	}

	@Override
	public Slice<Product> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status, Pageable pageable) {
		return productJpaRepository.findAllByIdInAndProductStatusNot(productIds, status, pageable)
			.map(ProductEntity::toDomain);
	}

	@Override
	public List<Product> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status) {
		return productJpaRepository.findAllByIdInAndProductStatusNot(productIds, status).stream()
			.map(ProductEntity::toDomain)
			.toList();
	}
}
