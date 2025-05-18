package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductServiceIntegrationTest {
	@Autowired
	private ProductService productService;

	@MockBean
	private ApiUserResolver apiUserResolver;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductPhotoRepository productPhotoRepository;

	@Test
	@DisplayName("사용자가 등록한 상품 목록 조회 테스트 - 성공")
	void findMyProducts_success() {
		// Given
		Long userId = 1L;
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);

		Product product1 = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		Product product2 = productRepository.save(Product.create(1L, "상품2", 20000, "설명2"));
		Product product3 = productRepository.save(Product.create(1L, "상품3", 30000, "설명3"));

		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product1.getId()).photoUrl("image1.jpg").order(0).build()));
		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product2.getId()).photoUrl("image2.jpg").order(0).build()));
		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product3.getId()).photoUrl("image3.jpg").order(0).build()));

		// When
		Slice<ProductThumbnail> result = productService.findMyProducts(1, 10);

		// Then: 최신순으로 정렬
		List<ProductThumbnail> thumbnails = result.getContent();
		assertThat(thumbnails).hasSize(3);
		assertThat(thumbnails.get(0).title()).isEqualTo("상품3");
		assertThat(thumbnails.get(1).title()).isEqualTo("상품2");
		assertThat(thumbnails.get(2).title()).isEqualTo("상품1");
	}
}
