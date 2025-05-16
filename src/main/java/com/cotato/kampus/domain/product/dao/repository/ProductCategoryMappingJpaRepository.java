package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.kampus.domain.product.dao.entity.ProductCategoryMappingEntity;

public interface ProductCategoryMappingJpaRepository extends JpaRepository<ProductCategoryMappingEntity, Long> {

	@Query("SELECT p.productId FROM ProductCategoryMappingEntity p WHERE p.categoryId = :categoryId")
	List<Long> findAllProductIdsByCategoryId(Long categoryId);

	void deleteAllByProductId(Long productId);
}
