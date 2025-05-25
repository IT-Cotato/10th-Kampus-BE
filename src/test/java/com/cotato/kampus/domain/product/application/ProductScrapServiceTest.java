package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.Assertions.*;
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
import com.cotato.kampus.domain.product.domain.ProductScrap;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.domain.product.implement.port.ProductScrapRepository;
import com.cotato.kampus.domain.product.implement.product.ProductManager;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductScrapServiceTest {
	@Autowired
	private ProductScrapService productScrapService;

	@MockBean
	private ApiUserResolver apiUserResolver;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductScrapRepository productScrapRepository;

	@Autowired
	private ProductPhotoRepository productPhotoRepository;

	@Autowired
	private ProductManager productManager;

	@Test
	@DisplayName("스크랩 추가 테스트 - 성공")
	void addScrap_success() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));

		// When
		productScrapService.addScrap(product.getId());

		// Then
		boolean isScrapped = productScrapRepository.existsByProductIdAndUserId(product.getId(), user.id());
		assertThat(isScrapped).isTrue();
		assertThat(productRepository.findById(product.getId()).get().getScrapCount()).isEqualTo(1);
	}

	@Test
	@DisplayName("스크렙 추가 테스트 - 이미 스크랩한 상품 예외")
	void addScrap_alreadyScrapped() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		productScrapRepository.save(ProductScrap.builder().productId(product.getId()).userId(user.id()).build());

		// When & Then
		assertThatThrownBy(() -> productScrapService.addScrap(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.ALREADY_SCRAPPED_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("스크렙 추가 테스트 - 삭제된 상품 예외")
	void addScrap_deletedProduct() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		productRepository.save(product.withProductStatus(ProductStatus.DELETED));

		// When & Then
		assertThatThrownBy(() -> productScrapService.addScrap(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("스크랩 삭제 테스트 - 성공")
	void removeScrap_success() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		productScrapRepository.save(ProductScrap.builder().productId(product.getId()).userId(user.id()).build());
		productManager.update(product.increaseScrapCount());

		// When
		productScrapService.removeScrap(product.getId());

		// Then
		boolean isScrapped = productScrapRepository.existsByProductIdAndUserId(product.getId(), user.id());
		assertThat(isScrapped).isFalse();
		assertThat(productRepository.findById(product.getId()).get().getScrapCount()).isEqualTo(0);
	}

	@Test
	@DisplayName("스크랩 삭제 테스트 - 스크랩 안된 상품 예외")
	void removeScrap_notScrapped() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));

		// When & Then
		assertThatThrownBy(() -> productScrapService.removeScrap(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.PRODUCT_SCRAP_NOT_FOUND.getMessage());

	}


	@Test
	@DisplayName("스크랩 목록 조회 테스트")
	void findScrapProducts_success() {
		// Given
		Long userId = 1L;
		int page = 1;
		int size = 10;

		Product product1 = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		Product product2 = productRepository.save(Product.create(2L, "상품2", 20000, "설명2"));
		Product product3 = productRepository.save(Product.create(3L, "상품3", 30000, "설명3"));

		productScrapRepository.save(ProductScrap.builder().productId(product1.getId()).userId(userId).build());
		productScrapRepository.save(ProductScrap.builder().productId(product2.getId()).userId(userId).build());
		productScrapRepository.save(ProductScrap.builder().productId(product3.getId()).userId(userId).build());

		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product1.getId()).photoUrl("image1.jpg").order(0).build()));
		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product2.getId()).photoUrl("image2.jpg").order(0).build()));
		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product3.getId()).photoUrl("image3.jpg").order(0).build()));

		given(apiUserResolver.getCurrentUserId()).willReturn(userId);

		// When
		Slice<ProductThumbnail> result = productScrapService.findScrapProducts(page, size);

		// Then
		List<ProductThumbnail> thumbnails = result.getContent();
		assertThat(thumbnails).hasSize(3);
		assertThat(thumbnails.get(0).title()).isEqualTo("상품3");
		assertThat(thumbnails.get(1).title()).isEqualTo("상품2");
		assertThat(thumbnails.get(2).title()).isEqualTo("상품1");
	}
}
