package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapFinder;
import com.cotato.kampus.domain.product.implement.productScrap.ProductScrapManager;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.helper.TestUserHelper;

public class ProductScrapTest extends ProductServiceTest {

	@Mock
	private ProductScrapFinder productScrapFinder;

	@Mock
	private ProductScrapManager productScrapManager;

	@Nested
	@DisplayName("상품 스크랩 테스트")
	class ScrapProductTest {

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

	@Nested
	@DisplayName("상품 스크랩 취소 테스트")
	class UnScrapProductTest {

		@Test
		@DisplayName("상품 스크랩 취소 성공")
		void removeScrap_success() {
			// Given: 유효한 상품과 스크랩이 존재할 때
			Long productId = 100L;
			Long userId = 1L;
			UserDto user = TestUserHelper.createUserDto(userId, 1L, UserRole.VERIFIED);

			Product product = spy(Product.fromEntity( // 스크랩 수가 5인 상품
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
			));

			Product unscrapedProduct = product.decreaseScrapCount();

			given(apiUserResolver.getCurrentUserDto()).willReturn(user);
			given(productFinder.findById(productId)).willReturn(product);
			given(productScrapFinder.isScrapped(productId, userId)).willReturn(true);
			given(product.decreaseScrapCount()).willReturn(unscrapedProduct);
			given(productSaver.update(any(Product.class))).willReturn(unscrapedProduct);
			willDoNothing().given(productScrapManager).delete(productId, userId);

			// When
			productService.removeScrap(productId);

			// Then: 스크랩 취소되고 모든 의존성 호출
			then(apiUserResolver).should().getCurrentUserDto();
			then(productFinder).should().findById(productId);
			then(productScrapFinder).should().isScrapped(productId, userId);
			then(productScrapManager).should().delete(productId, userId);
			then(productSaver).should().update(argThat(updatedProduct ->
				updatedProduct.getScrapCount() == product.getScrapCount() - 1));
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
			given(productScrapFinder.isScrapped(productId, userId)).willReturn(false);

			// When & Then: 예외 발생
			assertThatThrownBy(() -> productService.removeScrap(productId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_SCRAP_NOT_FOUND.getMessage());

			then(productScrapManager).should(never()).delete(anyLong(), anyLong());
			then(productSaver).should(never()).update(any(Product.class));
		}
	}
}