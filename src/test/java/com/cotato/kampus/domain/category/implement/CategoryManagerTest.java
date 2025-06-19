package com.cotato.kampus.domain.category.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.category.domain.Category;
import com.cotato.kampus.domain.category.implement.port.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryManagerTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryManager categoryManager;

	@Test
	@DisplayName("카테고리 추가 성공 테스트")
	void append_Success() {
		// given
		String categoryName = "테스트 카테고리";

		Category savedCategory = Category.builder()
			.id(1L)
			.categoryName(categoryName)
			.build();

		when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

		// when
		Category result = categoryManager.append(categoryName);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getCategoryName()).isEqualTo(categoryName);

		verify(categoryRepository).save(any(Category.class));
	}

}