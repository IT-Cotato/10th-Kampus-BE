package com.cotato.kampus.domain.product.implement.port;

import java.util.Optional;

import com.cotato.kampus.domain.product.domain.ProductCategory;

public interface ProductCategoryRepository {

	Optional<ProductCategory> findByCategoryName(String categoryName);

}
