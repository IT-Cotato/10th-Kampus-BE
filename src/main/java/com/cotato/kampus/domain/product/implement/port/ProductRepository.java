package com.cotato.kampus.domain.product.implement.port;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.enums.ProductStatus;

public interface ProductRepository {

	Product save(Product product);

	Optional<Product> findById(Long productId);

	Slice<Product> findAllByProductStatusNot(ProductStatus status, Pageable pageable);

	Slice<Product> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status, Pageable pageable);

	List<Product> findAllByIdInAndProductStatusNot(List<Long> productIds, ProductStatus status);

	Slice<Product> findAllByUserIdAndProductStatusNot(Long userId, ProductStatus status, Pageable pageable);

	Slice<Product> searchAll(String keyword, Pageable pageable);

	Slice<Product> findByKeywordWithStatusPriority(String keyword, Pageable pageable);
}
