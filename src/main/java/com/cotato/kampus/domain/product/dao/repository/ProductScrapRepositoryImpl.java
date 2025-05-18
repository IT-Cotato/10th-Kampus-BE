package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.dao.entity.ProductScrapEntity;
import com.cotato.kampus.domain.product.domain.ProductScrap;
import com.cotato.kampus.domain.product.implement.port.ProductScrapRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductScrapRepositoryImpl implements ProductScrapRepository {

	private final ProductScrapJpaRepository productScrapJpaRepository;

	@Override
	public boolean existsByProductIdAndUserId(Long productId, Long userId) {
		return productScrapJpaRepository.existsByProductIdAndUserId(productId, userId);
	}

	@Override
	public void save(ProductScrap productScrap) {
		ProductScrapEntity entity = ProductScrapEntity.fromDomain(productScrap);
		productScrapJpaRepository.save(entity);
	}

	@Override
	public void deleteByProductIdAndUserId(Long productId, Long userId) {
		productScrapJpaRepository.deleteByProductIdAndUserId(productId, userId);
	}

	@Override
	public Slice<ProductScrap> findAllByUserId(Long userId, Pageable pageable) {
		return productScrapJpaRepository.findAllByUserId(userId, pageable)
			.map(ProductScrapEntity::toDomain);
	}
}
