package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.dao.entity.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

	Slice<ProductEntity> findAllByProductStatusNot(ProductStatus status, Pageable pageable);

	Slice<ProductEntity> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status, Pageable pageable);

	List<ProductEntity> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status);
}
