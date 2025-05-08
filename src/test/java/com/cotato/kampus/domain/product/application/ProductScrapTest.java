package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.product.ProductFinder;
import com.cotato.kampus.domain.product.implement.product.ProductManager;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

@ExtendWith(MockitoExtension.class)
public class ProductScrapTest {

	@InjectMocks
	protected ProductService productService;

	@Mock
	protected ProductManager productManager;

	@Mock
	private ProductScrapFinder productScrapFinder;

	@Mock
	private ProductScrapManager productScrapManager;

	@Mock
	protected ApiUserResolver apiUserResolver;

	@Mock
	protected UserValidator userValidator;

	@Mock
	protected ProductFinder productFinder;

	@Nested
	@DisplayName("상품 스크랩 테스트")
	class ScrapProductTest {

		@BeforeEach
		void setUp() {
			// 각 테스트 전에 모든 Mock 초기화
			Mockito.reset(productScrapFinder, productScrapManager, productFinder,
				productManager, apiUserResolver, userValidator);
		}

		@Test
		@DisplayName("상품 스크랩 성공")
		void addScrap_success() {
			// Given: 유효한 사용자와 스크랩되지 않은 상품이 주어졌을 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = Product.create(userId, "빈티지 카메라", 10000, "상태 좋아요!");
			Product scrappedProduct = product.increaseScrapCount();

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(userValidator.validateStudentVerification(user)).willReturn(user.universityId());
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(productId, userId)).willReturn(false);
			given(productManager.update(any(Product.class))).willReturn(scrappedProduct);
			willDoNothing().given(productScrapManager).append(productId, userId);

			// When: 스크랩 요청 실행
			productService.addScrap(productId);

			// Then: 상품이 정상적으로 업데이트 되었는지 검증
			ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
			verify(productManager).update(productCaptor.capture());

			Product capturedProduct = productCaptor.getValue();
			assertThat(capturedProduct.getScrapCount()).isEqualTo(1);
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
			then(productManager).should(never()).update(any(Product.class));
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
			given(productScrapFinder.isScrapped(anyLong(), anyLong())).willReturn(true);


			// When & Then: 예외 발생
			assertThatThrownBy(() -> productService.addScrap(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.ALREADY_SCRAPPED_PRODUCT.getMessage());

			then(productScrapManager).should(never()).append(anyLong(), anyLong());
			then(productManager).should(never()).update(any(Product.class));
		}

	}

	@Nested
	@DisplayName("상품 스크랩 취소 테스트")
	class UnScrapProductTest {

		@BeforeEach
		void setUp() {
			// 각 테스트 전에 모든 Mock 초기화
			Mockito.reset(productScrapFinder, productScrapManager, productFinder,
				productManager, apiUserResolver, userValidator);
		}

		@Test
		@DisplayName("상품 스크랩 취소 성공")
		void removeScrap_success() {
			// Given: 유효한 상품과 스크랩이 존재할 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = Product.fromEntity( // 스크랩 수가 5인 상품
				productId,
				userId,
				"빈티지 카메라",          // title
				10000,                  // price
				"상태 좋아요!",           // description
				0,                      // viewCount
				5,                      // scrapCount
				0,                      // chatCount
				0,                      // bumpCount
				LocalDateTime.now(),    // bumpedTime
				ProductStatus.ACTIVE,  // status
				LocalDateTime.now(),    // createdTime
				LocalDateTime.now()     // lastModifiedTime
			);

			Product unscrapedProduct = product.decreaseScrapCount();

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(anyLong(), anyLong())).willReturn(true);
			given(productManager.update(any(Product.class))).willReturn(unscrapedProduct);
			willDoNothing().given(productScrapManager).delete(productId, userId);

			// When
			productService.removeScrap(productId);

			// Then
			ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
			verify(productManager).update(productCaptor.capture());
			Product capturedProduct = productCaptor.getValue();

			assertThat(capturedProduct.getScrapCount()).isEqualTo(4);
		}

		@Test
		@DisplayName("스크랩하지 않은 상품 스크랩 취소 실패")
		void removeScrap_fail_notScrapped() {
			// Given: 스크랩하지 않은 상품일 때
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
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(anyLong(), anyLong())).willReturn(false);

			// When & Then: 예외 발생
			assertThatThrownBy(() -> productService.removeScrap(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_SCRAP_NOT_FOUND.getMessage());

			then(productScrapManager).should(never()).delete(anyLong(), anyLong());
			then(productManager).should(never()).update(any(Product.class));
		}
	}
}