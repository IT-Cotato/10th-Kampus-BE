package com.cotato.kampus.domain.product.implement.port;

import java.util.List;
import java.util.Optional;

import com.cotato.kampus.domain.product.domain.ProductCategory;

public interface ProductCategoryRepository {

	Optional<ProductCategory> findByCategoryName(String categoryName);

	ProductCategory save(ProductCategory productCategory);

	boolean existsByCategoryName(String categoryName);

	List<ProductCategory> findAllByIdIn(List<Long> categoryIds);

	List<ProductCategory> findAll();

	Optional<ProductCategory> findById(Long categoryId);
}
