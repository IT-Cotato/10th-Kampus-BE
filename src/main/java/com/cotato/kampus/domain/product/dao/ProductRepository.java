package com.cotato.kampus.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
