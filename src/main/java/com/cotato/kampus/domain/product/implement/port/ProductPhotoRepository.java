package com.cotato.kampus.domain.product.implement.port;

import java.util.List;

import com.cotato.kampus.domain.product.domain.ProductPhoto;

public interface ProductPhotoRepository {

	List<ProductPhoto> findAllByProductId(Long productId);

	void saveAll(List<ProductPhoto> productPhotos);

	ProductPhoto findByProductIdAndOrder(Long productId, int order);

	void deleteAllByProductId(Long productId);

}
