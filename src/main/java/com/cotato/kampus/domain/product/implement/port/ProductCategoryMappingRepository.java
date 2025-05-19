package com.cotato.kampus.domain.product.implement.port;

import java.util.List;

import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;

public interface ProductCategoryMappingRepository {

	ProductCategoryMapping save(ProductCategoryMapping productCategoryMapping);

	List<Long> findAllProductIdsByCategoryId(Long productCategoryId);

	void deleteAllByProductId(Long productId);
}
