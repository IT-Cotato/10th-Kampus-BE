package com.cotato.kampus.domain.product.implement.port;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.product.domain.ProductScrap;

public interface ProductScrapRepository {

	boolean existsByProductIdAndUserId(Long productId, Long userId);

	void save (ProductScrap productScrap);

	void deleteByProductIdAndUserId(Long productId, Long userId);

	Slice<ProductScrap> findAllByUserId(Long userId, Pageable pageable);
}
