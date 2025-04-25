package com.cotato.kampus.domain.category.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.port.CategoryRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class CategoryFinderTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryFinder categoryFinder;

	@Test
	@DisplayName("ID로 카테고리 조회 성공 테스트")
	void find_ById_Success() {
		// given
		Long categoryId = 1L;
		Category expectedCategory = Category.builder()
			.id(categoryId)
			.categoryName("테스트 카테고리")
			.build();

		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));

		// when
		Category result = categoryFinder.find(categoryId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(categoryId);
		assertThat(result.getCategoryName()).isEqualTo("테스트 카테고리");
	}

	@Test
	@DisplayName("ID로 카테고리 조회 실패 테스트 - 존재하지 않는 카테고리")
	void find_ById_Failure_NotFound() {
		// given
		Long nonExistentCategoryId = 999L;
		when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> categoryFinder.find(nonExistentCategoryId))
			.isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
	}

	@Test
	@DisplayName("카테고리명으로 카테고리 조회 성공 테스트")
	void find_ByName_Success() {
		// given
		String categoryName = "테스트 카테고리";
		Category expectedCategory = Category.builder()
			.id(1L)
			.categoryName(categoryName)
			.build();

		when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(expectedCategory));

		// when
		Category result = categoryFinder.find(categoryName);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getCategoryName()).isEqualTo(categoryName);
	}

	@Test
	@DisplayName("카테고리명으로 카테고리 조회 실패 테스트 - 존재하지 않는 카테고리")
	void find_ByName_Failure_NotFound() {
		// given
		String nonExistentCategoryName = "존재하지 않는 카테고리";
		when(categoryRepository.findByCategoryName(nonExistentCategoryName)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> categoryFinder.find(nonExistentCategoryName))
			.isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_FOUND);
	}

	@Test
	@DisplayName("전체 카테고리 조회 테스트")
	void findAll_Success() {
		// given
		List<Category> expectedCategories = Arrays.asList(
			Category.builder().id(1L).categoryName("카테고리1").build(),
			Category.builder().id(2L).categoryName("카테고리2").build(),
			Category.builder().id(3L).categoryName("카테고리3").build()
		);

		when(categoryRepository.findAll()).thenReturn(expectedCategories);

		// when
		List<Category> results = categoryFinder.findAll();

		// then
		assertThat(results).isNotNull();
		assertThat(results).hasSize(3);
		assertThat(results).isEqualTo(expectedCategories);
	}
}