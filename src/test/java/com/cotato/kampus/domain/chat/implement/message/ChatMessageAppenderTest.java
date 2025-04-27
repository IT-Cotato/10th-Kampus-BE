package com.cotato.kampus.domain.chat.implement.message;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.implement.message.port.ChatMessageRepository;

@ExtendWith(MockitoExtension.class)
class ChatMessageAppenderTest {

	@InjectMocks
	private ChatMessageAppender target;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Test
	@DisplayName("채팅메시지 생성 성공_일반 메시지")
	public void appendChatMessage() {
		// given
		Long senderId = 1L;
		Long chatRoomId = 1L;
		boolean isImage = false;
		String content = "Hello, World!";

		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatRoomId)
			.senderId(senderId)
			.isImage(isImage)
			.content(content)
			.build();

		when(chatMessageRepository.save(any(ChatMessage.class)))
			.thenReturn(chatMessage);

		// when
		ChatMessage result = target.appendChatMessage(senderId, chatRoomId, isImage, content);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getChatroomId()).isEqualTo(chatRoomId);
		assertThat(result.getSenderId()).isEqualTo(senderId);
		assertThat(result.isImage()).isEqualTo(isImage);
		assertThat(result.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("채팅메시지 생성 성공_이미지 메시지")
	public void appendImageMessage() {
		// given
		Long senderId = 1L;
		Long chatRoomId = 1L;
		boolean isImage = true;
		String content = "image_url";

		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatRoomId)
			.senderId(senderId)
			.isImage(isImage)
			.content(content)
			.build();

		when(chatMessageRepository.save(any(ChatMessage.class)))
			.thenReturn(chatMessage);

		// when
		ChatMessage result = target.appendChatMessage(senderId, chatRoomId, isImage, content);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getChatroomId()).isEqualTo(chatRoomId);
		assertThat(result.getSenderId()).isEqualTo(senderId);
		assertThat(result.isImage()).isEqualTo(isImage);
		assertThat(result.getContent()).isEqualTo(content);
	}
}