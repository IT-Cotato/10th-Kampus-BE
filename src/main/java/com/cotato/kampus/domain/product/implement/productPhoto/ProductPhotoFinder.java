package com.cotato.kampus.domain.product.implement.productPhoto;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductPhotoFinder {

	private final ProductPhotoRepository productPhotoRepository;

	public List<ProductPhoto> findAll(Long productId) {
		return productPhotoRepository.findAllByProductId(productId);
	}

	public String findFirstPhoto(Long productId) {
		return productPhotoRepository.findByProductIdAndOrder(productId, 0)
			.getPhotoUrl();
	}
}
