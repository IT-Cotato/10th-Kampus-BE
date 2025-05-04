package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductCategoryMappingEntity;

public interface ProductCategoryMappingJpaRepository extends JpaRepository<ProductCategoryMappingEntity, Long> {
}
