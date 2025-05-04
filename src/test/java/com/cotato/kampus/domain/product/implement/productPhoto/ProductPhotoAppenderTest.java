package com.cotato.kampus.domain.product.implement.productPhoto;

import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductPhotoAppenderTest {

	@InjectMocks
	private ProductPhotoAppender productPhotoAppender;

	@Mock
	private ProductPhotoRepository productPhotoRepository;

	@Test
	@DisplayName("상품 사진 목록을 순서대로 저장")
	void appendAll_success() {
		// Given: 상품 ID와 여러 개의 사진 URL이 주어졌을 때
		Long productId = 1L;
		List<String> photoUrls = Arrays.asList(
			"https://example.com/photo1.jpg",
			"https://example.com/photo2.jpg",
			"https://example.com/photo3.jpg"
		);

		// When
		productPhotoAppender.appendAll(productId, photoUrls);

		// Then: 올바른 순서로 ProductPhoto가 생성되어 저장된다
		ArgumentCaptor<List<ProductPhoto>> photoCaptor = ArgumentCaptor.forClass(List.class);
		then(productPhotoRepository).should().saveAll(photoCaptor.capture());

		List<ProductPhoto> savedPhotos = photoCaptor.getValue();
		assertThat(savedPhotos)
			.hasSize(3)
			.extracting(ProductPhoto::getProductId)
			.containsOnly(productId);

		assertThat(savedPhotos.get(0).getPhotoUrl()).isEqualTo("https://example.com/photo1.jpg");
		assertThat(savedPhotos.get(0).getOrder()).isEqualTo(0);

		assertThat(savedPhotos.get(1).getPhotoUrl()).isEqualTo("https://example.com/photo2.jpg");
		assertThat(savedPhotos.get(1).getOrder()).isEqualTo(1);

		assertThat(savedPhotos.get(2).getPhotoUrl()).isEqualTo("https://example.com/photo3.jpg");
		assertThat(savedPhotos.get(2).getOrder()).isEqualTo(2);
	}
}