package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.product.dao.entity.ProductEntity;
import com.cotato.kampus.domain.product.enums.ProductStatus;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

	Slice<ProductEntity> findAllByProductStatusNot(ProductStatus status, Pageable pageable);

	Slice<ProductEntity> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status,
		Pageable pageable);

	List<ProductEntity> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status);

	Slice<ProductEntity> findAllByUserIdAndProductStatusNot(Long userId, ProductStatus status, Pageable pageable);

	@Query("SELECT p FROM ProductEntity p WHERE p.title LIKE %:keyword% OR p.description LIKE %:keyword%")
	Slice<ProductEntity> searchAll(String keyword, Pageable pageable);

	@Query("""
		    SELECT p FROM ProductEntity p\s
		    WHERE p.productStatus != 'DELETED'
		    AND (p.title LIKE %:keyword% OR p.description LIKE %:keyword%)
		    ORDER BY\s
		        CASE\s
		            WHEN p.productStatus = 'ACTIVE' THEN 0
		            WHEN p.productStatus = 'RESERVED' THEN 1
		            WHEN p.productStatus = 'SOLD' THEN 2
		            ELSE 3
		        END,
		        p.createdTime DESC
		""")
	Slice<ProductEntity> findByKeywordWithStatusPriority(@Param("keyword") String keyword, Pageable pageable);
}
