package com.cotato.kampus.domain.product.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.dao.ProductPhotoRepository;
import com.cotato.kampus.domain.product.domain.ProductPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ProductPhotoAppender {

	private final ProductPhotoRepository productPhotoRepository;

	@Transactional
	public void appendAll(Long productId, List<String> imageFiles) {
		List<ProductPhoto> productPhotos = imageFiles.stream()
			.map(imageFile -> ProductPhoto.builder()
				.productId(productId)
				.productPhotoUrl(imageFile)
				.productThumbnailUrl(imageFile)
				.build())
			.toList();

		productPhotoRepository.saveAll(productPhotos);
	}

}

