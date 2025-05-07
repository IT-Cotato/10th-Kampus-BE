package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductScrapEntity;

public interface ProductScrapJpaRepository extends JpaRepository<ProductScrapEntity, Long> {

	boolean existsByProductIdAndUserId(Long productId, Long userId);

}
