package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductPhotoEntity;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductPhotoRepositoryImpl implements ProductPhotoRepository {

	private final ProductPhotoJpaRepository productPhotoJpaRepository;

	@Override
	public List<ProductPhoto> findAllByProductId(Long productId) {
		return productPhotoJpaRepository.findByProductId(productId).stream()
			.map(ProductPhotoEntity::toDomain)
			.toList();
	}

	@Override
	public void saveAll(List<ProductPhoto> productPhotos) {
		List<ProductPhotoEntity> entities = productPhotos.stream()
			.map(ProductPhotoEntity::fromDomain)
			.toList();
		productPhotoJpaRepository.saveAll(entities);
	}

	@Override
	public ProductPhoto findByProductIdAndOrder(Long productId, int order) {
		return productPhotoJpaRepository.findByProductIdAndOrder(productId, order)
			.orElseThrow(() -> new AppException(ErrorCode.PRODUCT_PHOTO_NOT_FOUND))
			.toDomain();
	}

	@Override
	public void deleteAllByProductId(Long productId) {
		productPhotoJpaRepository.deleteAllByProductId(productId);
	}
}
