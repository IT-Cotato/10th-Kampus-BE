package com.cotato.kampus.domain.chat.implement.chatroom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;

@ExtendWith(MockitoExtension.class)
class ChatRoomFinderTest {

	@InjectMocks
	private ChatRoomFinder chatRoomFinder;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Test
	@DisplayName("유저가 받은 채팅방 개수 조회 - 성공")
	void findProductChatCount_Success() {
		// Given
		Long userId = 1L;
		Long productId = 1L;
		int expectedCount = 5;
		when(chatRoomRepository.countByInitialReceiverIdAndReferenceIdAndChatType(userId, productId, ChatType.PRODUCT))
			.thenReturn(expectedCount);

		// When
		int actualCount = chatRoomFinder.findProductChatCount(userId, productId, ChatType.PRODUCT);

		// Then
		assertEquals(expectedCount, actualCount);
	}

}