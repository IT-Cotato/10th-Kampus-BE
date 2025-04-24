package com.cotato.kampus.domain.chat.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.cotato.kampus.domain.chat.api.port.ChatMessageService;
import com.cotato.kampus.domain.chat.api.port.ChatRoomService;
import com.cotato.kampus.domain.chat.api.request.ChatMessageRequest;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatNotification;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;
import com.cotato.kampus.global.error.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ChatMessageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ChatMessageService chatMessageService;

	@MockitoBean
	private SimpMessagingTemplate messagingTemplate;

	@MockitoBean
	private ChatRoomService chatRoomService;

	private ChatMessageController controller;

	@BeforeEach
	void setUp() {
		controller = new ChatMessageController(chatMessageService, messagingTemplate);
	}

	@ParameterizedTest
	@DisplayName("채팅 메시지 전송 성공")
	@MethodSource("chatMessageTestCases")
	void sendMessage_Success(String testName, boolean isImage, String message) {
		// Given
		Long chatroomId = 1L;
		Long senderId = 123L;
		Long receiverId = 456L;

		ChatMessageRequest request = new ChatMessageRequest(chatroomId, senderId, isImage, message);

		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatroomId)
			.senderId(senderId)
			.isImage(isImage)
			.content(message)
			.build();

		ChatNotification chatNotification = ChatNotification.from(chatMessage, 5L);

		given(chatMessageService.processNewMessage(eq(chatroomId), eq(isImage), eq(message)))
			.willReturn(new ChatNotificationResult(chatMessage, chatNotification, receiverId));

		// When
		controller.sendMessage(chatroomId, request);

		// Then
		verify(chatMessageService).processNewMessage(chatroomId, isImage, message);
		verify(messagingTemplate).convertAndSend("/chatrooms/" + chatroomId, chatMessage);
		verify(messagingTemplate).convertAndSendToUser(
			receiverId.toString(),
			"/notifications/chat",
			chatNotification
		);
	}

	private static Stream<Arguments> chatMessageTestCases() {
		return Stream.of(
			Arguments.of("이미지 메시지 전송", true, "http://example.com/image.jpg"),
			Arguments.of("텍스트 메시지 전송", false, "Hello, World!")
		);
	}

	@Test
	@DisplayName("이미지가 비어 있는 경우 에러 발생")
	void uploadImage_EmptyImageList_ThrowsImageException() throws Exception {
		// Given
		Long chatroomId = 1L;

		// When & Then
		mockMvc.perform(multipart("/v1/api/chats/chatrooms/{chatroomId}/images", chatroomId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.FILE_IS_EMPTY.getCode()));
	}

	@Test
	@DisplayName("이미지가 11개 이상인 경우 에러 발생")
	void uploadImage_TooManyImages_ThrowsImageException() throws Exception {
		// Given
		Long chatroomId = 1L;

		// 11개의 이미지 생성 및 요청 구성
		List<MockMultipartFile> mockFiles = new ArrayList<>();
		for (int i = 1; i <= 11; i++) {
			mockFiles.add(new MockMultipartFile(
				"images",
				"test" + i + ".jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"test image content".getBytes()
			));
		}

		// When & Then
		mockMvc.perform(multipart("/v1/api/chats/chatrooms/{chatroomId}/images", chatroomId)
				.file(mockFiles.get(0))
				.file(mockFiles.get(1))
				.file(mockFiles.get(2))
				.file(mockFiles.get(3))
				.file(mockFiles.get(4))
				.file(mockFiles.get(5))
				.file(mockFiles.get(6))
				.file(mockFiles.get(7))
				.file(mockFiles.get(8))
				.file(mockFiles.get(9))
				.file(mockFiles.get(10))
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(ErrorCode.IMAGE_SIZE_EXCEEDED.getCode()));
	}

	@Test
	@DisplayName("이미지 업로드 성공")
	void uploadImage_Success() throws Exception {
		// Given
		Long chatroomId = 1L;

		// 유효한 이미지 파일 생성
		MockMultipartFile image1 = new MockMultipartFile(
			"images",
			"valid1.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		MockMultipartFile image2 = new MockMultipartFile(
			"images",
			"valid2.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		MockMultipartFile image3 = new MockMultipartFile(
			"images",
			"valid3.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		// Mock 서비스 응답 설정 - BDDMockito 사용
		List<String> uploadedImageUrls = List.of(
			"http://example.com/images/valid1.jpg",
			"http://example.com/images/valid2.jpg",
			"http://example.com/images/valid3.jpg"
		);

		given(chatMessageService.uploadImage(eq(chatroomId), anyList()))
			.willReturn(uploadedImageUrls);

		// When & Then
		mockMvc.perform(multipart("/v1/api/chats/chatrooms/{chatroomId}/images", chatroomId)
				.file(image1)
				.file(image2)
				.file(image3)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
			.andExpect(jsonPath("$.data.imageUrls[0]").value(uploadedImageUrls.get(0)))
			.andExpect(jsonPath("$.data.imageUrls[1]").value(uploadedImageUrls.get(1)))
			.andExpect(jsonPath("$.data.imageUrls[2]").value(uploadedImageUrls.get(2)));
	}
}