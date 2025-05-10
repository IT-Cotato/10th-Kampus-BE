package com.cotato.kampus.domain.product.implement.port;

import java.util.Optional;

import com.cotato.kampus.domain.product.domain.Product;

public interface ProductRepository {

	Product save(Product product);

	Optional<Product> findById(Long productId);
}
