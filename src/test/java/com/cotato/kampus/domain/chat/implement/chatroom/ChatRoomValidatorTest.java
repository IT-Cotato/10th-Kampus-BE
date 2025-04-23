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
	private ChatRoomValidator chatRoomValidator;

	@Mock
	private ChatRoomFinder chatRoomFinder;

	@Test
	@DisplayName("채팅방 중복 검증 - 중복된 채팅방이 있는 경우 예외 발생")
	public void validateDuplicateChatRoom() {
		// given
		Long postId = 1L;
		Long senderId = 1L;
		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId)).thenReturn(true);

		// when
		// then
		assertThatThrownBy(() -> chatRoomValidator.validateDuplicateChatRoom(postId, senderId))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.CHATROOM_DUPLICATED.getMessage());
	}

	@Test
	@DisplayName("채팅방 중복 검증 - 중복된 채팅방이 없는 경우 예외 발생하지 않음")
	public void validateDuplicateChatRoom_NoDuplicate() {
		// given
		Long postId = 1L;
		Long senderId = 1L;
		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId)).thenReturn(false);

		// when
		// then
		assertThatCode(() -> chatRoomValidator.validateDuplicateChatRoom(postId, senderId))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("채팅방 조회 - 채팅방에 들어가 있지 않은 유저가 조회하는 경우 예외 발생")
	public void validateEnteredUser() {
		// given
		Long userId = 1L;
		Long chatroomId = 1L;
		ChatRoom chatRoom = mock(ChatRoom.class);
		when(chatRoomFinder.findByChatRoomId(chatroomId)).thenReturn(chatRoom);
		doThrow(new AppException(ErrorCode.CHATROOM_NOT_ENTERED))
			.when(chatRoom).validateEnteredUser(userId);

		// when
		// then
		assertThatThrownBy(() -> chatRoomValidator.validateEnteredUser(userId, chatroomId))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.CHATROOM_NOT_ENTERED.getMessage());
	}

	@Test
	@DisplayName("채팅방 조회 - 채팅방에 들어가 있는 유저가 조회하는 경우 예외 발생하지 않음")
	public void validateEnteredUser_EnteredUser() {
		// given
		Long userId = 1L;
		Long chatroomId = 1L;
		ChatRoom chatRoom = mock(ChatRoom.class);
		when(chatRoomFinder.findByChatRoomId(chatroomId)).thenReturn(chatRoom);
		doNothing().when(chatRoom).validateEnteredUser(userId);

		// when
		// then
		assertThatCode(() -> chatRoomValidator.validateEnteredUser(userId, chatroomId))
			.doesNotThrowAnyException();
	}
}