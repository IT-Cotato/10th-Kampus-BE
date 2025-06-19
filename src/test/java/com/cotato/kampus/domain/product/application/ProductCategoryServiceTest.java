package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductCategoryInfo;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryManager;
import com.cotato.kampus.domain.user.application.UserValidator;

@ExtendWith(MockitoExtension.class)
public class ProductCategoryServiceTest {

	@Mock
	private ProductCategoryFinder productCategoryFinder;

	@Mock
	private UserValidator userValidator;

	@Mock
	private ProductCategoryManager productCategoryManager;

	@InjectMocks
	private ProductCategoryService productCategoryService;

	@Test
	@DisplayName("카테고리 조회 테스트 - 성공")
	void findAllCategories_success() {
		// Given
		ProductCategory category1 = ProductCategory.builder().categoryName("카테고리1").build();
		ProductCategory category2 = ProductCategory.builder().categoryName("카테고리2").build();

		when(productCategoryFinder.findAll()).thenReturn(List.of(category1, category2));

		// When
		List<ProductCategoryInfo> result = productCategoryService.findAllCategories();

		// Then
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).productCategoryId()).isEqualTo(category1.getId());
		assertThat(result.get(0).categoryName()).isEqualTo("카테고리1");
		assertThat(result.get(1).productCategoryId()).isEqualTo(category2.getId());
		assertThat(result.get(1).categoryName()).isEqualTo("카테고리2");

		verify(productCategoryFinder).findAll();
	}
}
