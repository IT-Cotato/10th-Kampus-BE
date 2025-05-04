package com.cotato.kampus.domain.product.implement.productCategory;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.implement.port.ProductCategoryRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ProductCategoryFinderTest {

	@InjectMocks
	private ProductCategoryFinder productCategoryFinder;

	@Mock
	private ProductCategoryRepository productCategoryRepository;

	@Nested
	@DisplayName("상품 카테고리 조회 성공 테스트")
	class FindCategorySuccessTest {

		@Test
		@DisplayName("존재하는 카테고리 이름으로 상품 카테고리 조회")
		void find_by_categoryName() {
			// Given: 존재하는 카테고리 이름이 주어졌을 때
			String categoryName = "전자제품";
			ProductCategory expectedCategory = ProductCategory.builder()
				.id(1L)
				.categoryName(categoryName)
				.build();

			given(productCategoryRepository.findByCategoryName(categoryName))
				.willReturn(Optional.of(expectedCategory));

			// When: 해당 카테고리를 찾으면
			ProductCategory foundCategory = productCategoryFinder.find(categoryName);

			// Then: 카테고리가 정상적으로 조회됨
			assertThat(foundCategory).isNotNull();
			assertThat(foundCategory.getId()).isEqualTo(1L);
			assertThat(foundCategory.getCategoryName()).isEqualTo(categoryName);
		}
	}

	@Nested
	@DisplayName("상품 카테고리 조회 실패 테스트")
	class FindCategoryFailureTest {

		@Test
		@DisplayName("존재하지 않는 카테고리 이름으로 조회")
		void find_fail_categoryName_notExists() {
			// Given: 존재하지 않는 카테고리 이름이 주어졌을 때
			String nonExistentCategory = "존재하지않는카테고리";

			given(productCategoryRepository.findByCategoryName(nonExistentCategory))
				.willReturn(Optional.empty());

			// When & Then: 카테고리를 찾으려고 하면 예외가 발생한다
			assertThatThrownBy(() -> productCategoryFinder.find(nonExistentCategory))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND.getMessage());
		}
	}
}