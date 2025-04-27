package com.cotato.kampus.domain.post.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cotato.kampus.domain.post.application.PostSearchService;

@WebMvcTest(PostSearchController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PostSearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PostSearchService postSearchService;

	@Test
	@DisplayName("검색 기록 전체 삭제 성공 케이스")
	void deleteAllHistory() throws Exception {
		doNothing().when(postSearchService).deleteAllSearchKeyword();
		mockMvc.perform(delete("/v1/api/posts/search/keywords"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"));
	}
}