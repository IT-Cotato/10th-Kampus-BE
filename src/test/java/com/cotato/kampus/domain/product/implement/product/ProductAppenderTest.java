package com.cotato.kampus.domain.product.implement.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.ProductStatus;
import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAppenderTest {

	@InjectMocks
	private ProductAppender productAppender;

	@Mock
	private ProductRepository productRepository;

	@Nested
	@DisplayName("상품 등록 테스트")
	class AppendProductTest {

		@Test
		@DisplayName("상품 등록 성공")
		void append_success() {
			// Given: 필수 정보들이 주어졌을 때
			Long userId = 1L;
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";

			Product expectProduct = Product.builder()
				.id(1L)
				.userId(userId)
				.title(title)
				.price(price)
				.description(description)
				.status(ProductStatus.ACTIVE)
				.build();

			given(productRepository.save(any(Product.class))).willReturn(expectProduct);

			// When: 상품을 생성하면
			Product result = productAppender.append(userId, title, price, description);

			// Then: 정상적으로 상품이 생성된다
			assertThat(result).isNotNull();
			assertThat(result.getUserId()).isEqualTo(userId);
			assertThat(result.getTitle()).isEqualTo(title);
			assertThat(result.getPrice()).isEqualTo(price);
			assertThat(result.getDescription()).isEqualTo(description);
			assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVE);
			assertThat(result.getViewCount()).isEqualTo(0);
			assertThat(result.getScrapCount()).isEqualTo(0);
			assertThat(result.getChatCount()).isEqualTo(0);
			assertThat(result.getBumpCount()).isEqualTo(0);
		}

		@Test
		@DisplayName("userId가 null이면 상품 생성 실패")
		void append_fail_userId_null() {
			Long userId = null;
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = "상태 좋아요!";

			assertThatThrownBy(() -> productAppender.append(userId, title, price, description))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_USER_ID_REQUIRED.getMessage());
		}

		@Test
		@DisplayName("title이 빈 문자열이면 상품 생성 실패")
		void append_fail_title_empty() {
			Long userId = 1L;
			String title = " ";
			Integer price = 10000;
			String description = "상태 좋아요!";

			assertThatThrownBy(() -> productAppender.append(userId, title, price, description))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_TITLE_REQUIRED.getMessage());
		}

		@Test
		@DisplayName("price가 null이면 상품 생성 실패")
		void append_fail_price_null() {
			Long userId = 1L;
			String title = "빈티지 카메라";
			Integer price = null;
			String description = "상태 좋아요!";

			assertThatThrownBy(() -> productAppender.append(userId, title, price, description))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_PRICE_REQUIRED.getMessage());
		}

		@Test
		@DisplayName("price가 음수면 상품 생성 실패")
		void append_fail_price_invalid() {
			Long userId = 1L;
			String title = "빈티지 카메라";
			Integer price = -10000;
			String description = "상태 좋아요!";

			assertThatThrownBy(() -> productAppender.append(userId, title, price, description))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_PRICE_INVALID.getMessage());
		}

		@Test
		@DisplayName("description이 빈 문자열이면 상품 생성 실패")
		void append_fail_description_empty() {
			Long userId = 1L;
			String title = "빈티지 카메라";
			Integer price = 10000;
			String description = " ";

			assertThatThrownBy(() -> productAppender.append(userId, title, price, description))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_DESCRIPTION_REQUIRED.getMessage());
		}


	}

}