package com.cotato.kampus.domain.chat.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cotato.kampus.domain.chat.api.port.ChatRoomService;
import com.cotato.kampus.domain.chat.api.request.ChatroomRequest;
import com.cotato.kampus.domain.chat.domain.ChatRoomDetailDto;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cotato.kampus.domain.chat.api.validator.ValidChatSearchTypeValidator;

@WebMvcTest(controllers = ChatRoomController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@org.springframework.context.annotation.Import(ValidChatSearchTypeValidator.class)
class ChatRoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ChatRoomService chatRoomService;

	private ChatRoomPreviewList mockChatRoomPreviewList;
	private ChatRoomDetailDto mockChatRoomDetailDto;

	@BeforeEach
	public void init() {
		// Mock 데이터 초기화
		List<ChatRoomPreview> previewList = new ArrayList<>();
		previewList.add(new ChatRoomPreview(1L, "테스트 채팅방 1", 1L, "안녕하세요", LocalDateTime.now(), 0L));
		previewList.add(new ChatRoomPreview(2L, "테스트 채팅방 2", 2L, "반갑습니다", LocalDateTime.now(), 2L));

		mockChatRoomPreviewList = ChatRoomPreviewList.from(previewList, false);

		mockChatRoomDetailDto = new ChatRoomDetailDto(
			1L, 1L, "테스트 채팅방 1", 1L, "게시판명", 1L, 2L, false);
	}

	@Test
	@DisplayName("postId를 통해 채팅방을 생성한다.")
	void createChatroomTest() throws Exception {
		// given
		Long postId = 1L;
		ChatType chatType = ChatType.POST;
		ChatroomRequest request = new ChatroomRequest(postId);

		given(chatRoomService.createChatRoom(postId, chatType)).willReturn(1L);

		mockMvc.perform(post("/v1/api/chats/chatrooms")
				.param("type", String.valueOf("POST"))
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.data").isNotEmpty());
	}

	@Test
	@DisplayName("postId가 없으면 채팅방 생성에 실패한다.")
	void createChatroomWithoutPostId() throws Exception {
		// given
		ChatroomRequest request = new ChatroomRequest(null);

		mockMvc.perform(post("/v1/api/chats/chatrooms")
				.param("type", String.valueOf("POST"))
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@MethodSource("chatTypeProvider")
	@DisplayName("채팅 타입별로 내가 속한 채팅방을 조회한다.")
	void getChatRoomsTest(String chatTypeStr, ChatType chatType) throws Exception {
		// given
		int page = 1;
		given(chatRoomService.findChatRooms(page, chatType)).willReturn(mockChatRoomPreviewList);
		given(chatRoomService.findChatRooms(page, null)).willReturn(mockChatRoomPreviewList);

		// when & then
		mockMvc.perform(get("/v1/api/chats/chatrooms")
				.param("page", String.valueOf(page))
				.param("type", chatTypeStr))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.data.chatRoomPreviewList").isArray())
			.andExpect(jsonPath("$.data.chatRoomPreviewList.length()").value(2));
	}

	// ParameterizedTest를 위한 메서드 소스
	private static Stream<Arguments> chatTypeProvider() {
		return Stream.of(
			Arguments.of("POST", ChatType.POST),
			Arguments.of("PRODUCT", ChatType.PRODUCT),
			Arguments.of("ALL", null));
	}

	@Test
	@DisplayName("채팅방 상세 정보를 조회한다.")
	void getChatRoomDetailTest() throws Exception {
		// given
		Long chatroomId = 1L;
		given(chatRoomService.getChatRoomDetail(chatroomId)).willReturn(mockChatRoomDetailDto);

		// when & then
		mockMvc.perform(get("/v1/api/chats/chatrooms/{chatroomId}", chatroomId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.data.chatroomId").value(1))
			.andExpect(jsonPath("$.data.title").value("테스트 채팅방 1"));
	}

	@Test
	@DisplayName("채팅방을 삭제한다.")
	void deleteChatroomTest() throws Exception {
		// given
		Long chatroomId = 1L;
		doNothing().when(chatRoomService).deleteChatroom(chatroomId);

		// when & then
		mockMvc.perform(delete("/v1/api/chats/chatrooms/{chatroomId}", chatroomId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("OK"));
	}

	@Test
	@DisplayName("채팅방 리스트 조회시 유효하지 않은 타입을 요청하면 BadRequest를 반환한다.")
	void getChatRooms_InvalidType_BadRequest() throws Exception {
		mockMvc.perform(get("/v1/api/chats/chatrooms")
				.param("type", "INVALID"))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("채팅방 생성시 ALL 타입을 요청하면 BadRequest를 반환한다.")
	void createChatroom_InvalidType_BadRequest() throws Exception {
		ChatroomRequest request = new ChatroomRequest(1L);

		mockMvc.perform(post("/v1/api/chats/chatrooms")
				.param("type", "ALL")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}