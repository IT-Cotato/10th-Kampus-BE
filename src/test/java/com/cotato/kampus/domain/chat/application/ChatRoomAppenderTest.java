package com.cotato.kampus.domain.chat.application;

import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;

@ExtendWith(MockitoExtension.class)
class ChatRoomAppenderTest {

	@InjectMocks
	private ChatRoomAppender target;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Test
	public void 채팅방_생성() {
		// given
		ChatRoom chatRoom = ChatRoom.builder()
			.postId(1L)
			.initialSenderId(1L)
			.initialReceiverId(2L)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();

		// private 변수에 접근할 수 있음
		ReflectionTestUtils.setField(chatRoom, "id", 123L);
		// when
		when(chatRoomRepository.save(Mockito.any(ChatRoom.class)))
			.thenReturn(chatRoom.getId());

		Long id = target.appendChatRoom(1L, 1L, 2L);
		Assertions.assertThat(id).isEqualTo(123L);
	}
}