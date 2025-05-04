package com.cotato.kampus.domain.product.implement.productPhoto;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.product.dao.repository.ProductPhotoJpaRepository;
import com.cotato.kampus.domain.product.domain.ProductPhoto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ProductPhotoAppender {

	private final ProductPhotoJpaRepository productPhotoRepository;

	@Transactional
	public void appendAll(Long productId, List<String> photoUrls) {
		List<ProductPhoto> productPhotoPhotos = IntStream.range(0, photoUrls.size())
			.mapToObj(i -> ProductPhoto.builder()
				.productId(productId)
				.photoUrl(photoUrls.get(i))
				.order(i)
				.build())
			.toList();

		productPhotoRepository.saveAll(productPhotoPhotos);
	}
}

