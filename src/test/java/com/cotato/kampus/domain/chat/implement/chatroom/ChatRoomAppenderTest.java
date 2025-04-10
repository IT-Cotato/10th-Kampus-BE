package com.cotato.kampus.domain.chat.implement.chatroom;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatRoomAppenderTest {

	@InjectMocks
	private ChatRoomAppender chatRoomAppender;

	@Mock
	private ChatRoomRepository chatRoomRepository;

	@Nested
	@DisplayName("채팅방 생성 테스트")
	class AppendChatRoomTest {

		@Test
		@DisplayName("채팅방 생성 성공")
		public void appendChatRoom_success() {
			// given
			Long postId = 1L;
			Long senderId = 2L;
			Long receiverId = 3L;
			Long expectedChatRoomId = 100L;

			given(chatRoomRepository.save(any(ChatRoom.class))).willReturn(expectedChatRoomId);

			// when
			Long result = chatRoomAppender.appendChatRoom(postId, senderId, receiverId);

			// then
			assertThat(result).isEqualTo(expectedChatRoomId);
		}

		@Test
		@DisplayName("채팅방 생성 실패 - 보내는 사람과 받는 사람이 동일한 경우")
		public void appendChatRoom_fail_sameUser() {
			// given
			Long postId = 1L;
			Long sameUserId = 2L;

			// when
			// then
			assertThatThrownBy(() -> chatRoomAppender.appendChatRoom(postId, sameUserId, sameUserId))
				.isInstanceOf(AppException.class)
				.hasMessage(ErrorCode.INVALID_CHATROOM.getMessage());
		}
	}
}