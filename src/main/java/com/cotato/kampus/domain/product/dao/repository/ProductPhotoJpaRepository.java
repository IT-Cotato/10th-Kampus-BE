package com.cotato.kampus.domain.product.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.dao.entity.ProductPhotoEntity;
import com.cotato.kampus.domain.product.domain.ProductPhoto;

public interface ProductPhotoJpaRepository extends JpaRepository<ProductPhoto, Long> {

	List<ProductPhotoEntity> findByProductId(Long productId);
}
