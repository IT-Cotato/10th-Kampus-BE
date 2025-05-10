package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductPhotoEntity;

public interface ProductPhotoJpaRepository extends JpaRepository<ProductPhotoEntity, Long> {

	List<ProductPhotoEntity> findByProductId(Long productId);

	ProductPhotoEntity findByProductIdAndOrder(Long productId, int order);
}
