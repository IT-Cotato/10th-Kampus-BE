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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductCategoryMapping;
import com.cotato.kampus.domain.product.domain.ProductDetails;
import com.cotato.kampus.domain.product.domain.ProductPhoto;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.enums.ProductSortType;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryMappingRepository;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;
import com.cotato.kampus.domain.product.implement.port.ProductPhotoRepository;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.product.ProductManager;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
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
public class ProductServiceTest {
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

	@Autowired
	private ProductManager productManager;

	@Autowired
	private ProductScrapManager productScrapManager;

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
	@DisplayName("상품 삭제 테스트 - 성공")
	void delete_success() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));

		// When
		productService.deleteProduct(product.getId());

		// Then
		Product deletedProduct = productFinder.findById(product.getId());
		assertThat(deletedProduct.getStatus()).isEqualTo(ProductStatus.DELETED);
	}

	@Test
	@DisplayName("상품 삭제 테스트 - 권한이 없는 유저 예외")
	void delete_unauthorized() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		// 다른 유저의 상품
		Product product = productRepository.save(Product.create(2L, "상품1", 10000, "설명1"));

		// When & Then
		assertThatThrownBy(() -> productService.deleteProduct(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.FORBIDDEN_PRODUCT_EDIT.getMessage());

		Product unchangedProduct = productFinder.findById(product.getId());
		assertThat(unchangedProduct.getStatus()).isNotEqualTo(ProductStatus.DELETED);
	}

	@Test
	@DisplayName("상품 삭제 테스트 - 이미 삭제된 상품 예외")
	void delete_alreadyDeleted() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		// 이미 삭제된 상품
		Product product = productRepository.save(Product.create(1L, "상품1", 10000, "설명1"));
		productManager.update(product.withProductStatus(ProductStatus.DELETED));

		// When & Then
		assertThatThrownBy(() -> productService.deleteProduct(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("상품 수정 테스트 - 성공")
	void update_success() throws ImageException {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserId()).willReturn(user.id());

		// 초기 카테고리 & 상품 저장
		ProductCategory category1 = productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		ProductCategory category2 =productCategoryRepository.save(ProductCategory.builder().categoryName("의류").build());

		Product product = productRepository.save(Product.create(user.id(), "상품1", 10000, "설명1"));
		productCategoryMappingRepository.save(ProductCategoryMapping.builder().categoryId(category1.getId()).productId(product.getId()).build());
		productPhotoRepository.saveAll(List.of(ProductPhoto.builder().productId(product.getId()).photoUrl("image1.jpg").order(0).build()));

		// 새로운 카테고리
		List<String> newCategoryNames = List.of("의류");

		// 새 이미지
		List<MultipartFile> newImages = List.of(
			new MockMultipartFile("image2", "image2.jpg", "image/jpeg", "image-content".getBytes())
		);
		List<String> newImageUrls = List.of("https://s3.bucket/new-image2.jpg");
		given(s3Uploader.uploadFiles(anyList(), anyString())).willReturn(newImageUrls);

		// When
		productService.updateProduct(product.getId(), "상품수정", 20000, "수정된 설명", newCategoryNames, newImages);

		// Then
		Product updatedProduct = productFinder.findById(product.getId());
		assertThat(updatedProduct.getTitle()).isEqualTo("상품수정");
		assertThat(updatedProduct.getPrice()).isEqualTo(20000);
		assertThat(updatedProduct.getDescription()).isEqualTo("수정된 설명");

		List<Long> updatedCategoryIds = productCategoryMappingRepository.findAllCategoryIdByProductId(product.getId());
		assertThat(updatedCategoryIds).containsExactly(category2.getId());

		List<ProductPhoto> updatedPhotos = productPhotoRepository.findAllByProductId(product.getId());
		assertThat(updatedPhotos).hasSize(1);
		assertThat(updatedPhotos.get(0).getPhotoUrl()).isEqualTo("https://s3.bucket/new-image2.jpg");

	}

	@Test
	@DisplayName("상품 수정 테스트 - 권한 없는 유저 예외")
	void update_unauthorized() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserId()).willReturn(user.id());

		// 다른 유저의 상품
		Product product = productRepository.save(Product.create(2L, "상품1", 10000, "설명1"));

		// 새로운 카테고리 & 이미지
		List<String> newCategoryNames = List.of("의류");
		List<MultipartFile> newImages = List.of(
			new MockMultipartFile("image2", "image2.jpg", "image/jpeg", "image-content".getBytes())
		);

		// When & Then
		assertThatThrownBy(
			() -> productService.updateProduct(product.getId(), "상품수정", 20000, "수정된 설명", newCategoryNames, newImages))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.FORBIDDEN_PRODUCT_EDIT.getMessage());

		Product unchangedProduct = productFinder.findById(product.getId());
		assertThat(unchangedProduct.getTitle()).isEqualTo("상품1");
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void findProductDetails_success() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		ProductCategory category = productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());

		Product product = productRepository.save(Product.create(user.id(), "노트북", 100000, "노트북입니다."));
		productCategoryMappingRepository.save(ProductCategoryMapping.builder()
			.categoryId(category.getId())
			.productId(product.getId())
			.build());

		productPhotoRepository.saveAll(List.of(ProductPhoto.builder()
			.productId(product.getId())
			.photoUrl("https://bucket/image1.jpg")
			.order(0)
			.build()));

		productScrapManager.append(product.getId(), user.id());

		// When
		ProductDetails result = productService.findProductDetails(product.getId());

		// Then
		assertThat(result.title()).isEqualTo("노트북");
		assertThat(result.photos().get(0).photoUrl()).isEqualTo("https://bucket/image1.jpg");
		assertThat(result.categories().get(0)).isEqualTo("전자제품");
		assertThat(result.isScrapped()).isTrue();
		assertThat(result.isAuthor()).isTrue();
		assertThat(result.viewCount()).isEqualTo(1);
	}

	@Test
	@DisplayName("상품 상세 조회 - 삭제된 상품 예외")
	void findProductDetail_deletedProduct() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		Product product = productRepository.save(Product.create(user.id(), "노트북", 100000, "노트북입니다."));
		productManager.update(product.withProductStatus(ProductStatus.DELETED));

		// When & Then
		assertThatThrownBy(() -> productService.findProductDetails(product.getId()))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());

		assertThat(product.getViewCount()).isEqualTo(0);

	}

	@Test
	@DisplayName("상품 목록 조회 - 성공 (카테고리 X)")
	void findProducts_success() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		for(int i = 0; i < 10; i++) {
			Product product = productRepository.save(Product.create(user.id(), "상품" + i, 10000, "설명" + i));
			productPhotoRepository.saveAll(List.of(ProductPhoto.builder()
				.productId(product.getId())
				.photoUrl("image" + i + ".jpg")
				.order(0)
				.build()));
		}

		int page = 1;
		int size = 5;

		// When
		Slice<ProductThumbnail> result = productService.findProducts(page, size, ProductSortType.recent, null);

		// Then
		assertThat(result.getContent()).hasSize(5);
		assertThat(result.getContent().get(0).photoUrl()).isEqualTo("image9.jpg"); // 가장 최신 상품의 사진
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공 (카테고리 O)")
	void findProducts_success_withCategory() {
		// Given
		UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);
		given(apiUserResolver.getCurrentUserDto()).willReturn(user);

		ProductCategory category1 = productCategoryRepository.save(ProductCategory.builder().categoryName("전자제품").build());
		ProductCategory category2 = productCategoryRepository.save(ProductCategory.builder().categoryName("의류").build());

		// 0 ~ 2번 상품은 전자제품, 3 ~ 9번 상품은 의류
		for(int i = 0; i < 10; i++) {
			Product product = productRepository.save(Product.create(user.id(), "상품" + i, 10000, "설명" + i));
			productPhotoRepository.saveAll(List.of(ProductPhoto.builder()
				.productId(product.getId())
				.photoUrl("image" + i + ".jpg")
				.order(0)
				.build()));

			if(i < 3) {
				productCategoryMappingRepository.save(ProductCategoryMapping.builder()
					.productId(product.getId()).categoryId(category1.getId()).build());
			} else {
				productCategoryMappingRepository.save(ProductCategoryMapping.builder()
					.productId(product.getId()).categoryId(category2.getId()).build());
			}
		}

		int page = 1;
		int size = 5;

		// When
		Slice<ProductThumbnail> result = productService.findProducts(page, size, ProductSortType.recent, "전자제품");

		// Then
		assertThat(result.getContent()).hasSize(3);
		assertThat(result.getContent().get(0).photoUrl()).isEqualTo("image2.jpg");
	}

	@Test
	@DisplayName("상품 상태 변경 테스트 - 성공")
	void updateStatus_success() {
		// Given
		Long userId = 1L;
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);
		Product product = productRepository.save(Product.create(userId, "상품1", 10000, "설명1"));

		// When
		productService.updateStatus(product.getId(), ProductStatus.SOLD);

		// Then
		Product updatedProduct = productFinder.findById(product.getId());
		assertThat(updatedProduct.getStatus()).isEqualTo(ProductStatus.SOLD);
	}

	@Test
	@DisplayName("상품 상태 변경 테스트 - 권한이 없는 유저 예외")
	void updateStatus_unauthorized() {
		// Given
		Long userId = 1L;
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);

		// 다른 유저의 상품
		Product product = productRepository.save(Product.create(2L, "상품1", 10000, "설명1"));

		// When & Then
		assertThatThrownBy(() -> productService.updateStatus(product.getId(), ProductStatus.SOLD))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.FORBIDDEN_PRODUCT_EDIT.getMessage());

		Product result = productFinder.findById(product.getId());
		assertThat(result.getStatus()).isNotEqualTo(ProductStatus.SOLD);
	}

	@Test
	@DisplayName("상품 상태 변경 테스트 - 삭제된 상품 예외")
	void updateStatus_deletedProduct() {
		// Given
		Long userId = 1L;
		given(apiUserResolver.getCurrentUserId()).willReturn(userId);
		Product product = productRepository.save(Product.create(userId, "상품1", 10000, "설명1"));
		productManager.update(product.withProductStatus(ProductStatus.DELETED));

		// When & Then
		assertThatThrownBy(() -> productService.updateStatus(product.getId(), ProductStatus.SOLD))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());

		Product result = productFinder.findById(product.getId());
		assertThat(result.getStatus()).isNotEqualTo(ProductStatus.SOLD);
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
