package com.cotato.kampus.domain.product.implement.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.Product;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.domain.product.implement.port.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductManagerTest {

	@InjectMocks
	private ProductManager productManager;

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

			Product expectProduct = Product.create(
				userId,
				title,
				price,
				description
			);

			given(productRepository.save(any(Product.class))).willReturn(expectProduct);

			// When: 상품을 생성하면
			Product result = productManager.append(userId, title, price, description);

			// Then: 정상적으로 상품이 생성된다
			assertThat(result).isNotNull();
			assertThat(result.getUserId()).isEqualTo(userId);
			assertThat(result.getTitle()).isEqualTo(title);
			assertThat(result.getPrice()).isEqualTo(price);
			assertThat(result.getDescription()).isEqualTo(description);
			assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVE);
			assertThat(result.getViewCount()).isEqualTo(0);
			assertThat(result.getScrapCount()).isEqualTo(0);
			assertThat(result.getBumpCount()).isEqualTo(0);
		}
	}

}