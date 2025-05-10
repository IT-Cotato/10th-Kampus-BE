package com.cotato.kampus.domain.category.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cotato.kampus.domain.category.api.request.CreateCategoryRequest;
import com.cotato.kampus.domain.category.application.CategoryService;
import com.cotato.kampus.domain.category.domain.Category;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper; // Java 객체를 문자열로 변환

	@Mock
	private CategoryService categoryService;

	@InjectMocks
	private CategoryController categoryController;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@DisplayName("카테고리 생성 API 테스트")
	void createCategory() throws Exception {
		// given
		String categoryName = "새 카테고리";
		Long categoryId = 1L;
		CreateCategoryRequest request = new CreateCategoryRequest(categoryName);
		when(categoryService.createCategory(categoryName)).thenReturn(categoryId);


		// when & then
		mockMvc.perform(post("/v1/api/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(categoryId));

		verify(categoryService).createCategory(categoryName);
	}

	@Test
	@DisplayName("카테고리 목록 조회 API 테스트")
	void findAllCategory() throws Exception {
		// given
		List<Category> categories = Arrays.asList(
			Category.builder().id(1L).categoryName("카테고리1").build(),
			Category.builder().id(2L).categoryName("카테고리2").build(),
			Category.builder().id(3L).categoryName("카테고리3").build()
		);

		when(categoryService.findAllCategory()).thenReturn(categories);

		// when & then
		mockMvc.perform(get("/v1/api/categories")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.categories").isArray())
			.andExpect(jsonPath("$.data.categories.length()").value(3))
			.andExpect(jsonPath("$.data.categories[0].id").value(1))
			.andExpect(jsonPath("$.data.categories[0].categoryName").value("카테고리1"))
			.andExpect(jsonPath("$.data.categories[1].id").value(2))
			.andExpect(jsonPath("$.data.categories[1].categoryName").value("카테고리2"))
			.andExpect(jsonPath("$.data.categories[2].id").value(3))
			.andExpect(jsonPath("$.data.categories[2].categoryName").value("카테고리3"));

		verify(categoryService).findAllCategory();
	}
}