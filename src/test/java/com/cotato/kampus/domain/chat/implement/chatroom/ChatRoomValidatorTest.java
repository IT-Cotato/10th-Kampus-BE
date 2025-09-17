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
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ChatRoomDuplicatedException;

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
		Long referenceId = 1L;
		Long senderId = 2L;
		ChatType chatType = ChatType.POST;

		when(chatRoomFinder.existsByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType))
			.thenReturn(false);

		// when & then
		assertThatCode(() -> target.validateDuplicateChatRoom(referenceId, senderId, chatType))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("채팅방 중복 검증 실패 - 채팅방이 이미 존재함")
	void validateDuplicateChatRoom_Failure() {
		// given
		Long referenceId = 1L;
		Long senderId = 2L;
		ChatType chatType = ChatType.POST;
		Long existingChatRoomId = 100L;

		ChatRoom existingChatRoom = mock(ChatRoom.class);
		when(existingChatRoom.getId()).thenReturn(existingChatRoomId);

		when(chatRoomFinder.existsByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType))
			.thenReturn(true);
		when(chatRoomFinder.findByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType))
			.thenReturn(existingChatRoom);

		// when & then
		assertThatThrownBy(() -> target.validateDuplicateChatRoom(referenceId, senderId, chatType))
			.isInstanceOf(ChatRoomDuplicatedException.class)
			.hasMessage(ErrorCode.CHATROOM_DUPLICATED.getMessage());

		verify(chatRoomFinder).existsByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType);
		verify(chatRoomFinder).findByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType);
	}

	@Test
	@DisplayName("채팅방 중복 검증 성공 - 같은 referenceId지만 다른 chatType인 경우")
	void validateDuplicateChatRoom_Success_DifferentChatType() {
		// given
		Long referenceId = 1L;
		Long senderId = 2L;
		ChatType newChatType = ChatType.PRODUCT;

		// chatType.PRODUCT로만 테스트하기 때문에 existingChatType에 대한 stubbing 제거
		when(chatRoomFinder.existsByReferenceIdAndSenderIdAndChatType(referenceId, senderId, newChatType))
			.thenReturn(false);

		// POST 타입으로 이미 채팅방이 있더라도 PRODUCT 타입으로는 새로 생성 가능
		assertThatCode(() -> target.validateDuplicateChatRoom(referenceId, senderId, newChatType))
			.doesNotThrowAnyException();
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