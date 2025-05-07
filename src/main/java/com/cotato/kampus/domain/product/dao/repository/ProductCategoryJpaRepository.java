package com.cotato.kampus.domain.product.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductCategoryEntity;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, Long> {

	Optional<ProductCategoryEntity> findByCategoryName(String categoryName);

	boolean existsByCategoryName(String categoryName);
}
