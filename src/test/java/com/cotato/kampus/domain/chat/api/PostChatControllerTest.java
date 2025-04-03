package com.cotato.kampus.domain.chat.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cotato.kampus.domain.chat.application.PostChatService;
import com.cotato.kampus.domain.chat.api.request.ChatroomRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PostChatController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PostChatControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private PostChatService postChatService;

	@MockitoBean
	private SimpMessagingTemplate messagingTemplate;

	@BeforeEach
	public void init() {
	}

	@Test
	@DisplayName("postId를 통해 채팅방을 생성한다.")
	void createChatroomTest() throws Exception {
		// given
		Long postId = 1L;
		ChatroomRequest request = new ChatroomRequest(postId);

		given(postChatService.createChatRoom(postId)).willReturn(1L);

		mockMvc.perform(post("/v1/api/chats/post")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.data").isNotEmpty());
	}

	@Test
	@DisplayName("postId가 없으면 채팅방 생성에 실패한다.")
	public void createChatroomWithoutPostId() throws Exception {
		// given
		ChatroomRequest request = new ChatroomRequest(null);

		mockMvc.perform(post("/v1/api/chats/post")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}