package com.cotato.kampus.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.domain.ProductPhoto;

public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
}
