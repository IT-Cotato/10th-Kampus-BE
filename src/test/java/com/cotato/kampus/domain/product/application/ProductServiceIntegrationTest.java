package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;
import com.cotato.kampus.helper.TestUserHelper;

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

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	private ProductCategoryMappingRepository productCategoryMappingRepository;

	@MockBean
	private S3Uploader s3Uploader;

	@Autowired
	private ProductFinder productFinder;

	@Test
	@DisplayName("상품 등록 테스트 - 성공")
	void create_success() throws ImageException {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		List<String> categoryNames = List.of("전자제품", "의류");
		productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		productCategoryRepository.save(ProductCategory.builder().categoryName("의류").build());

		List<MultipartFile> images = List.of(
			new MockMultipartFile("image1", "image1.jpg", "image/jpeg", "image-content".getBytes())
		);
		List<String> dummyUrls = List.of("https://s3.bucket/image1.jpg");
		given(s3Uploader.uploadFiles(anyList(), anyString())).willReturn(dummyUrls);

		// When
		Long productId = productService.createProduct("상품1", 10000, "설명1", categoryNames, images);

		// Then
		Product product = productFinder.findById(productId);
		assertThat(product.getTitle()).isEqualTo("상품1");
		assertThat(product.getPrice()).isEqualTo(10000);
		assertThat(product.getDescription()).isEqualTo("설명1");
		assertThat(product.getViewCount()).isEqualTo(0);

		List<Long> categoryIds = productCategoryMappingRepository.findAllCategoryIdByProductId(productId);
		assertThat(categoryIds.size()).isEqualTo(2);

		List<ProductPhoto> photos = productPhotoRepository.findAllByProductId(productId);
		assertThat(photos.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("상품 등록 테스트 - 재학 인증 안된 유저 예외")
	void create_userUnverified() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.UNVERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		List<String> categoryNames = List.of("전자제품");

		List<MultipartFile> images = List.of(
			new MockMultipartFile("image1", "image1.jpg", "image/jpeg", "image-content".getBytes())
		);

		// When & Then
		assertThatThrownBy(() -> productService.createProduct("상품1", 10000, "설명1", categoryNames, images))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.USER_UNVERIFIED.getMessage());
	}

	@Test
	@DisplayName("상품 등록 테스트 - 카테고리가 유효하지 않은 경우")
	void create_categoryInvalid() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		List<String> invalidCategoryNames = List.of("전자제품", "뷰티");

		List<MultipartFile> images = List.of(
			new MockMultipartFile("image1", "image1.jpg", "image/jpeg", "image-content".getBytes())
		);

		// When & Then
		assertThatThrownBy(() -> productService.createProduct("상품1", 10000, "설명1", invalidCategoryNames, images))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("상품 등록 테스트 - 이미지가 유효하지 않은 경우")
	void create_imageInvalid() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		List<String> categoryNames = List.of("전자제품");

		List<MultipartFile> images = List.of(
			new MockMultipartFile("image1", "image1.heic", "image/heic", "image-content".getBytes())
		);

		// When & Then
		assertThatThrownBy(() -> productService.createProduct("상품1", 10000, "설명1", categoryNames, images))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.INVALID_IMAGE_FORMAT.getMessage());
	}

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
