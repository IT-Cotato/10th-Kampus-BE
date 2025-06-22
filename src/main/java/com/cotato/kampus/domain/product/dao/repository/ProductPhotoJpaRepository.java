package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductPhotoEntity;

public interface ProductPhotoJpaRepository extends JpaRepository<ProductPhotoEntity, Long> {

	List<ProductPhotoEntity> findByProductId(Long productId);

	Optional<ProductPhotoEntity> findByProductIdAndOrder(Long productId, int order);

	void deleteAllByProductId(Long productId);
}
