package com.cotato.kampus.domain.post.api;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.cotato.kampus.domain.product.api.ProductSearchController;
import com.cotato.kampus.domain.product.application.ProductSearchService;
import com.cotato.kampus.domain.product.domain.ProductThumbnail;
import com.cotato.kampus.domain.product.enums.ProductStatus;
import com.cotato.kampus.global.common.dto.DataResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProductSearchController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductSearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductSearchService productSearchService;

	@Test
	@DisplayName("성공: 유효한 키워드로 상품 검색")
	void searchProducts_Success() throws Exception {
		// given
		String keyword = "맥북";

		Pageable pageable = PageRequest.of(0, 10);

		List<ProductThumbnail> thumbnails = List.of(
			new ProductThumbnail(
				1L,
				"맥북 프로 M1",
				1500000,
				"photo1.jpg",
				ProductStatus.ACTIVE,
				5,
				3,
				LocalDateTime.of(2024, 1, 1, 10, 0, 0),
				false
			)
		);

		when(productSearchService.searchProducts(keyword, 1))
			.thenReturn(new SliceImpl<>(thumbnails, pageable, false));

		// when
		MvcResult result = mockMvc.perform(get("/v1/api/products/search")
				.param("keyword", keyword)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String responseBody = result.getResponse().getContentAsString();
		DataResponse<?> response = objectMapper.readValue(responseBody, DataResponse.class);

		assertThat(response.getStatus()).isEqualTo("OK");
		assertThat(response.getData()).isNotNull();
	}

	@ParameterizedTest
	@MethodSource("provideInvalidKeywords")
	@DisplayName("실패: 유효하지 않은 키워드로 검색")
	void searchProducts_InvalidKeyword_Fail(String keyword) throws Exception {
		// when & then
		mockMvc.perform(get("/v1/api/products/search")
				.param("keyword", keyword)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@MethodSource("provideValidBoundaryKeywords")
	@DisplayName("성공: 경계값 키워드로 검색")
	void searchProducts_BoundaryKeyword_Success(String keyword) throws Exception {
		// given
		Pageable pageable = PageRequest.of(0, 10);
		when(productSearchService.searchProducts(anyString(), anyInt()))
			.thenReturn(new SliceImpl<>(List.of(), pageable, false));

		// when
		MvcResult result = mockMvc.perform(get("/v1/api/products/search")
				.param("keyword", keyword)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		// then
		String responseBody = result.getResponse().getContentAsString();
		DataResponse<?> response = objectMapper.readValue(responseBody, DataResponse.class);
		assertThat(response.getStatus()).isEqualTo("OK");
	}

	private static Stream<Arguments> provideInvalidKeywords() {
		return Stream.of(
			Arguments.of(""),           // 빈 문자열
			Arguments.of(" "),          // 공백
			Arguments.of("a"),          // 1자 (최소 미만)
			Arguments.of("12345678901") // 11자 (최대 초과)
		);
	}

	private static Stream<Arguments> provideValidBoundaryKeywords() {
		return Stream.of(
			Arguments.of("맥북"),         // 2자 (최소)
			Arguments.of("1234567890")   // 10자 (최대)
		);
	}
}