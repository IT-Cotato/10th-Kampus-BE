package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.product.ProductSaver;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryMappingAdapter;
import com.cotato.kampus.domain.product.implement.productPhoto.ProductPhotoAppender;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductSaver productSaver;

	@Mock
	private ProductPhotoAppender productPhotoAppender;

	@Mock
	private S3Uploader s3Uploader;

	@Mock
	private ApiUserResolver apiUserResolver;

	@Mock
	private UserValidator userValidator;

	@Mock
	private ImageValidator imageValidator;

	@Mock
	private ProductCategoryFinder productCategoryFinder;

	@Mock
	private ProductCategoryMappingAdapter productCategoryMappingAdapter;

	@Mock
	private ProductFinder productFinder;

	@Mock
	private ProductScrapFinder productScrapFinder;

	@Mock
	private ProductScrapManager productScrapManager;

	@Nested
	@DisplayName("상품 생성 성공 테스트")
	class CreateProductSuccessTest {
		@Test
		void create_success() throws ImageException {
			// Given: 모든 파라미터가 유효할 때
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";
			List<String> categoryNames = Arrays.asList("전자제품", "가전");
			List<MultipartFile> images = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class));

			UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);

			ProductCategory category1 = ProductCategory.builder()
				.id(10L)
				.categoryName("전자제품")
				.build();

			ProductCategory category2 = ProductCategory.builder()
				.id(20L)
				.categoryName("가전")
				.build();

			Product createdProduct = Product.fromEntity(
				100L,                    // id
				user.id(),              // userId
				title,                  // title
				price,                  // price
				description,            // description
				0,                      // viewCount
				0,                      // scrapCount
				0,                      // chatCount
				0,                      // bumpCount
				LocalDateTime.now(),    // bumpedTime
				ProductStatus.ACTIVE,   // status
				LocalDateTime.now(),    // createdTime
				LocalDateTime.now()     // lastModifiedTime
			);


			List<String> imageUrls = Arrays.asList("url1", "url2");

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productCategoryFinder.find("전자제품")).willReturn(category1);
			given(productCategoryFinder.find("가전")).willReturn(category2);
			willDoNothing().given(imageValidator).validateProductImages(images);
			given(s3Uploader.uploadFiles(images, "product")).willReturn(imageUrls);
			given(productSaver.append(1L, title, price, description)).willReturn(createdProduct);
			willDoNothing().given(productCategoryMappingAdapter).saveAll(100L, Arrays.asList(10L, 20L));
			willDoNothing().given(productPhotoAppender).appendAll(100L, imageUrls);

			// When: 상품을 생성하면
			Long productId = productService.createProduct(title, price, description, categoryNames, images);

			// Then: 상품이 정상적으로 생성되고 모든 의존성이 호출된다
			assertThat(productId).isEqualTo(100L);
			then(apiUserResolver).should().getCurrentUserDto();
			then(userValidator).should().validateStudentVerification(user);
			then(productCategoryFinder).should().find("전자제품");
			then(productCategoryFinder).should().find("가전");
			then(imageValidator).should().validateProductImages(images);
			then(s3Uploader).should().uploadFiles(images, "product");
			then(productSaver).should().append(1L, title, price, description);
			then(productCategoryMappingAdapter).should().saveAll(100L, Arrays.asList(10L, 20L));
			then(productPhotoAppender).should().appendAll(100L, imageUrls);

		}
	}

	@Nested
	@DisplayName("상품 생성 실패 테스트")
	class CreateProductFailureTest {

		@Test
		@DisplayName("재학 인증되지 않은 유저")
		void create_fail_userUnverified() throws ImageException {
			// Given: 인증되지 않은 유저일 때
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";
			List<String> categoryNames = Arrays.asList("전자제품");
			List<MultipartFile> images = Arrays.asList(mock(MultipartFile.class));

			UserDto unverifiedUser = TestUserHelper.createUserDto(1L, null, UserRole.UNVERIFIED);

			given(apiUserResolver.getCurrentUserDto()).willReturn(unverifiedUser);
			doThrow(new AppException(ErrorCode.USER_UNVERIFIED))
				.when(userValidator).validateStudentVerification(unverifiedUser);

			// When & Then: 예외가 발생해야 함
			assertThatThrownBy(() -> productService.createProduct(title, price, description, categoryNames, images))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.USER_UNVERIFIED.getMessage());
		}


		@Test
		@DisplayName("카테고리가 없는 경우")
		void create_fail_categoryEmpty() {
			// Given
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";
			List<String> categoryNames = Collections.emptyList();
			List<MultipartFile> images = Arrays.asList(mock(MultipartFile.class));

			UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());

			// When & Then
			assertThatThrownBy(() -> productService.createProduct(title, price, description, categoryNames, images))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_CATEGORY_REQUIRED.getMessage());

			then(productCategoryFinder).should(never()).find(anyString());
			then(productSaver).should(never()).append(anyLong(), anyString(), any(), anyString());
		}

		@Test
		@DisplayName("카테고리가 유효하지 않은 경우")
		void create_fail_categoryInvalid() {
			// Given
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";
			List<String> categoryNames = Arrays.asList("존재하지않는카테고리");
			List<MultipartFile> images = Arrays.asList(mock(MultipartFile.class));

			UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productCategoryFinder.find("존재하지않는카테고리"))
				.willThrow(new AppException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND));

			// When & Then
			assertThatThrownBy(() -> productService.createProduct(title, price, description, categoryNames, images))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND.getMessage());

			then(productSaver).should(never()).append(anyLong(), anyString(), any(), anyString());
		}

		@Test
		@DisplayName("유효하지 않은 이미지")
		void create_fail_imageInvalid() throws ImageException {
			// Given
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";
			List<String> categoryNames = Arrays.asList("전자제품");
			List<MultipartFile> images = Arrays.asList(mock(MultipartFile.class));

			UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);

			ProductCategory category = ProductCategory.builder()
				.id(1L)
				.categoryName("전자제품")
				.build();

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productCategoryFinder.find("전자제품")).willReturn(category);
			willThrow(new AppException(ErrorCode.PRODUCT_PHOTO_REQUIRED))
				.given(imageValidator).validateProductImages(images);

			// When & Then
			assertThatThrownBy(() -> productService.createProduct(title, price, description, categoryNames, images))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_PHOTO_REQUIRED.getMessage());

			then(s3Uploader).should(never()).uploadFiles(anyList(), anyString());
			then(productSaver).should(never()).append(anyLong(), anyString(), any(), anyString());

		}
	}

	@Nested
	@DisplayName("상품 삭제 테스트")
	class DeleteProductTest {

		@Test
		@DisplayName("상품 삭제 성공")
		void delete_success() {
			// Given: 유효한 상품과 사용자가 주어졌을 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = Product.create(
				userId,
				"빈티지 카메라",
				10000,
				"상태 좋아요!"
			);

			Product deletedProduct = product.withProductStatus(ProductStatus.DELETED);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(productFinder.findById(productId)).willReturn(product);
			given(productSaver.update(any(Product.class))).willReturn(deletedProduct);

			// When: 상품을 삭제하면
			productService.deleteProduct(productId);

			// Then: 상품 상태가 삭제로 변경되고 모든 의존성 호출됨
			then(apiUserResolver).should().getCurrentUserDto();
			then(productFinder).should().findById(productId);
			then(productSaver).should().update(argThat(updatedProduct ->
				updatedProduct.getStatus() == ProductStatus.DELETED));

		}

		@Test
		@DisplayName("삭제 권한이 없는 경우 실패")
		void delete_fail_unauthorized() {
			// Given: 상품 소유자가 아닐 때
			Long productId = 100L;
			Long productOwnerId = 1L;
			Long otherUserId = 2L;
			UserDto otherUser = TestUserHelper.createUserDto(otherUserId, 1L, UserRole.VERIFIED);

			// Product.validateDeletable() 메서드를 호출하기 위해 spy() 메서드로 실제 객체 생성
			Product product = spy(Product.create(
				productOwnerId,
				"빈티지 카메라",
				10000,
				"상태 좋아요!"
			));

			doThrow(new AppException(ErrorCode.FORBIDDEN_PRODUCT_DELETE))
				.when(product).validateDeletable(otherUserId);

			given(apiUserResolver.getCurrentUserDto()).willReturn(otherUser);
			given(productFinder.findById(productId)).willReturn(product);

			// When & Then: 예외 발생
			assertThatThrownBy(() -> productService.deleteProduct(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.FORBIDDEN_PRODUCT_DELETE.getMessage());

			then(productSaver).should(never()).update(any(Product.class));
		}

		@Test
		@DisplayName("이미 삭제된 상품 삭제 실패")
		void delete_fail_alreadyDeleted() {
			// Given
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(1L, 1L, UserRole.VERIFIED);

			Product deletedProduct = Product.fromEntity(
				productId,
				userId,
				"빈티지 카메라",          // title
				10000,                  // price
				"상태 좋아요!",           // description
				5,                      // viewCount
				2,                      // scrapCount
				1,                      // chatCount
				0,                      // bumpCount
				LocalDateTime.now(),    // bumpedTime
				ProductStatus.DELETED,  // status - 이미 삭제됨
				LocalDateTime.now(),    // createdTime
				LocalDateTime.now()     // lastModifiedTime
			);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(productFinder.findById(productId)).willReturn(deletedProduct);

			// When & Then
			assertThatThrownBy(() -> productService.deleteProduct(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());

			then(productSaver).should(never()).update(any(Product.class));
		}
	}

	@Nested
	@DisplayName("상품 스크랩 테스트")
	class ScrapeProductTest {

		@Test
		@DisplayName("상품 스크랩 성공")
		void addScrap_success() {
			// Given: 유효한 상품과 사용자가 주어졌을 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = Product.create(userId, "빈티지 카메라", 10000, "상태 좋아요!");

			Product scrappedProduct = product.increaseChatCount();

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(productId, userId)).willReturn(false);
			given(productSaver.update(any(Product.class))).willReturn(scrappedProduct);
			willDoNothing().given(productScrapManager).append(productId, userId);

			// When
			productService.addScrap(productId);

			// Then: 스크랩 추가되고 모든 의존성 호출
			then(apiUserResolver).should().getCurrentUserDto();
			then(userValidator).should().validateStudentVerification(user);
			then(productFinder).should().findById(productId);
			then(productScrapFinder).should().isScrapped(productId, userId);
			then(productScrapManager).should().append(productId, userId);
			then(productSaver).should().update(argThat(updatedProduct ->
				updatedProduct.getScrapCount() == product.getScrapCount() + 1));
		}

		@Test
		@DisplayName("삭제된 상품 스크랩 실패")
		void addScrap_fail_deletedProduct() {
			// Given: 삭제된 상품일 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product deletedProduct = Product.create(
				userId, "빈티지 카메라", 10000, "상태 좋아요!")
				.withProductStatus(ProductStatus.DELETED);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productFinder.findById(productId)).willReturn(deletedProduct);

			// When & Then
			assertThatThrownBy(() -> productService.addScrap(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.ALREADY_DELETED_PRODUCT.getMessage());

			then(productScrapFinder).should(never()).isScrapped(anyLong(), anyLong());
			then(productScrapManager).should(never()).append(anyLong(), anyLong());
			then(productSaver).should(never()).update(any(Product.class));
		}

		@Test
		@DisplayName("이미 스크랩한 상품 스크랩 실패")
		void addScrap_fail_alreadyScrapped() {
			// Given: 이미 스크랩한 상품일 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = Product.create(
				userId,
				"빈티지 카메라",
				10000,
				"상태 좋아요!"
			);

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(productId, userId)).willReturn(true);

			// When & Then: 예외 발생
			assertThatThrownBy(() -> productService.addScrap(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.ALREADY_SCRAPPED_PRODUCT.getMessage());

			then(productScrapManager).should(never()).append(anyLong(), anyLong());
			then(productSaver).should(never()).update(any(Product.class));
		}

	}
}