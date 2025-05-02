package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.domain.ProductPhoto;

public interface ProductPhotoJpaRepository extends JpaRepository<ProductPhoto, Long> {
}
