package com.cotato.kampus.domain.product.implement.port;

import java.util.List;

import com.cotato.kampus.domain.product.domain.ProductPhoto;

public interface ProductPhotoRepository {

	List<ProductPhoto> findByProductId(Long productId);

	void saveAll(List<ProductPhoto> productPhotos);
}
