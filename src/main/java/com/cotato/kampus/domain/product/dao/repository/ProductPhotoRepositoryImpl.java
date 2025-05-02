package com.cotato.kampus.domain.product.dao.repository;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductPhotoRepositoryImpl implements ProductPhotoRepository {

	private final ProductPhotoJpaRepository productPhotoJpaRepository;
}
