package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductPhotoEntity;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductPhotoRepositoryImpl implements ProductPhotoRepository {

	private final ProductPhotoJpaRepository productPhotoJpaRepository;

	@Override
	public List<ProductPhoto> findByProductId(Long productId) {
		return productPhotoJpaRepository.findByProductId(productId).stream()
			.map(ProductPhotoEntity::toDomain)
			.toList();
	}
}
