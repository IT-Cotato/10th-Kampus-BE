package com.cotato.kampus.domain.chat.application;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.implement.chatroom.ChatRoomAppender;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;

@ExtendWith(MockitoExtension.class)
class ChatRoomAppenderTest {

	@InjectMocks
	private ChatRoomAppender target;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Test
	@DisplayName("채팅방 생성 성공")
	public void 채팅방_생성() {
		// given
		ChatRoom chatRoom = ChatRoom.create(1L, 1L, 2L);

		// private 변수에 접근할 수 있음
		ReflectionTestUtils.setField(chatRoom, "id", 123L);

		// when
		given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(123L);
		Long id = target.appendChatRoom(1L, 1L, 2L);

		// then
		Assertions.assertThat(id).isNotNull();
		Assertions.assertThat(id).isEqualTo(123L);
	}
}