package com.cotato.kampus.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.product.domain.ProductCategory;
import com.cotato.kampus.domain.product.domain.ProductCategoryInfo;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryFinder;
import com.cotato.kampus.domain.product.implement.productCategory.ProductCategoryManager;
import com.cotato.kampus.domain.user.application.UserValidator;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

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

	@Test
	@DisplayName("카테고리 수정 테스트 - 성공")
	void updateCategories_success() {
		// given
		Long categoryId = 1L;
		String categoryName = "카테고리";
		String newCategoryName = "변경된 카테고리";

		ProductCategory category = ProductCategory.builder()
			.id(categoryId)
			.categoryName(categoryName)
			.build();

		doNothing().when(userValidator).validateAdminAccess();
		when(productCategoryFinder.find(categoryId)).thenReturn(category);

		// when
		productCategoryService.updateCategory(categoryId, newCategoryName);

		// then
		ArgumentCaptor<ProductCategory> captor = ArgumentCaptor.forClass(ProductCategory.class);
		verify(productCategoryManager).update(captor.capture());

		ProductCategory updatedCategory = captor.getValue();
		assertThat(newCategoryName).isEqualTo(updatedCategory.getCategoryName());

		verify(userValidator).validateAdminAccess();
		verify(productCategoryFinder).find(categoryId);
	}

	@Test
	@DisplayName("카테고리 수정 테스트 - 관리자 권한 없음 실패")
	void updateCategories_fail_notAdmin() {
		// given
		Long categoryId = 1L;
		String newCategoryName = "변경된 카테고리";

		doThrow(new AppException(ErrorCode.USER_NOT_ADMIN))
			.when(userValidator).validateAdminAccess();

		// when & then
		AppException exception = assertThrows(AppException.class,
			() -> productCategoryService.updateCategory(categoryId, newCategoryName));

		assertThat(exception)
			.extracting(AppException::getErrorCode)
			.isEqualTo(ErrorCode.USER_NOT_ADMIN);

		verify(userValidator).validateAdminAccess();
		verifyNoMoreInteractions(productCategoryFinder);
	}
}
