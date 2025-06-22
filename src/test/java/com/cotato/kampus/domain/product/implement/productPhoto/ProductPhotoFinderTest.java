package com.cotato.kampus.domain.product.implement.productPhoto;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.factory.ProductPhotoFactory;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ProductPhotoFinderTest {

	@InjectMocks
	private ProductPhotoFinder productPhotoFinder;

	@Mock
	private ProductPhotoRepository productPhotoRepository;

	@Test
	@DisplayName("상품 첫번째 사진 조회 - 성공")
	void findFirstPhoto_success() {
		// Given
		Long productId = 1L;
		String imageUrl = "https://example.com/photo1.jpg";

		ProductPhoto productPhoto = ProductPhotoFactory.createProductPhoto(1L, productId,
			imageUrl, 1);

		// When
		when(productPhotoRepository.findByProductIdAndOrder(productId, 0))
			.thenReturn(productPhoto);

		// Then
		assertThat(productPhotoFinder.findFirstPhoto(productId))
			.isEqualTo(imageUrl);
	}

	@Test
	@DisplayName("상품 첫번째 사진 조회 실패 - 상품 사진이 없는 경우")
	void findFirstPhoto_fail_noPhoto() {
		// Given
		Long productId = 1L;

		// When
		when(productPhotoRepository.findByProductIdAndOrder(productId, 0))
			.thenThrow(new AppException(ErrorCode.PRODUCT_NOT_FOUND));

		// Then
		assertThatThrownBy(() -> productPhotoFinder.findFirstPhoto(productId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
	}
}