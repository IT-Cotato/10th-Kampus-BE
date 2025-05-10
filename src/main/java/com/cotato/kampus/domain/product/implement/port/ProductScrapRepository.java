package com.cotato.kampus.domain.product.implement.port;

import com.cotato.kampus.domain.product.domain.ProductScrap;

public interface ProductScrapRepository {

	boolean existsByProductIdAndUserId(Long productId, Long userId);

	void save (ProductScrap productScrap);

	void deleteByProductIdAndUserId(Long productId, Long userId);
}
