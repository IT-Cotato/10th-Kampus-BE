package com.cotato.kampus.domain.chat.implement.chatroom;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatRoomValidatorTest {

	@InjectMocks
	private ChatRoomValidator target;

	@Mock
	private ChatRoomFinder chatRoomFinder;

	@Test
	@DisplayName("채팅방 중복 검증 성공 - 채팅방이 존재하지 않음")
	void validateDuplicateChatRoom_Success() {
		// given
		Long postId = 1L;
		Long senderId = 2L;

		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId))
			.thenReturn(false);

		// when & then
		assertThatCode(() -> target.validateDuplicateChatRoom(postId, senderId))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("채팅방 중복 검증 실패 - 채팅방이 이미 존재함")
	void validateDuplicateChatRoom_Failure() {
		// given
		Long postId = 1L;
		Long senderId = 2L;

		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId))
			.thenReturn(true);

		// when & then
		assertThatThrownBy(() -> target.validateDuplicateChatRoom(postId, senderId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_DUPLICATED.getMessage());
	}

	@Test
	@DisplayName("채팅방 사용자 검증 성공 - 채팅방에 참여한 사용자")
	void validateEnteredUser_Success() {
		// given
		Long chatroomId = 1L;
		Long userId = 2L;

		ChatRoom chatRoom = mock(ChatRoom.class);

		when(chatRoomFinder.findByChatRoomId(chatroomId))
			.thenReturn(chatRoom);

		doNothing().when(chatRoom).validateEnteredUser(userId);

		// when & then
		assertThatCode(() -> target.validateEnteredUser(userId, chatroomId))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("채팅방 사용자 검증 실패 - 채팅방에 참여하지 않은 사용자")
	void validateEnteredUser_Failure() {
		// given
		Long chatroomId = 1L;
		Long userId = 4L;

		ChatRoom chatRoom = mock(ChatRoom.class);

		when(chatRoomFinder.findByChatRoomId(chatroomId))
			.thenReturn(chatRoom);

		doThrow(new AppException(ErrorCode.CHATROOM_NOT_ENTERED))
			.when(chatRoom).validateEnteredUser(userId);

		// when & then
		assertThatThrownBy(() -> target.validateEnteredUser(userId, chatroomId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_NOT_ENTERED.getMessage());
	}
}