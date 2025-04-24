package com.cotato.kampus.domain.chat.implement.chatroom;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatRoomValidatorTest {

	@InjectMocks
	private ChatRoomValidator target;

	@Mock
	private ChatRoomFinder chatRoomFinder;

	@Test
	@DisplayName("새 채팅방 생성 검증 성공")
	void validateNewChatRoom_Success() {
		// given
		Long postId = 1L;
		Long senderId = 2L;
		Long receiverId = 3L;

		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId))
			.thenReturn(false);

		// when & then
		assertThatCode(() -> target.validateNewChatRoom(postId, senderId, receiverId))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("새 채팅방 생성 검증 실패 - 발신자와 수신자가 동일")
	void validateNewChatRoom_Failure_SameSenderAndReceiver() {
		// given
		Long postId = 1L;
		Long senderId = 2L;
		Long receiverId = 2L; // senderId와 동일

		// when & then
		assertThatThrownBy(() -> target.validateNewChatRoom(postId, senderId, receiverId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.INVALID_CHATROOM.getMessage());
	}

	@Test
	@DisplayName("새 채팅방 생성 검증 실패 - 이미 채팅방 존재")
	void validateNewChatRoom_Failure_ChatRoomAlreadyExists() {
		// given
		Long postId = 1L;
		Long senderId = 2L;
		Long receiverId = 3L;

		when(chatRoomFinder.existsByPostIdAndSenderId(postId, senderId))
			.thenReturn(true);

		// when & then
		assertThatThrownBy(() -> target.validateNewChatRoom(postId, senderId, receiverId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_DUPLICATED.getMessage());
	}

	@ParameterizedTest
	@DisplayName("채팅방 사용자 검증 성공 - 발신자/수신자 일치")
	@MethodSource("provideValidUserTestCases")
	void validateUser_Success(String scenarioName, Long userId, Long initialSenderId, Long initialReceiverId) {
		// given
		Long chatroomId = 1L;

		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatroomId)
			.postId(10L)
			.initialSenderId(initialSenderId)
			.initialReceiverId(initialReceiverId)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.createdTime(LocalDateTime.now())
			.lastModifiedTime(LocalDateTime.now())
			.build();

		when(chatRoomFinder.findByChatRoomId(chatroomId))
			.thenReturn(chatRoom);

		// when & then
		assertThatCode(() -> target.validateUser(userId, chatroomId))
			.doesNotThrowAnyException();
	}

	static Stream<Arguments> provideValidUserTestCases() {
		return Stream.of(
			Arguments.of("초기 발신자 일치", 2L, 2L, 3L),
			Arguments.of("초기 수신자 일치", 3L, 2L, 3L)
		);
	}

	@Test
	@DisplayName("채팅방 사용자 검증 실패 - 권한 없는 사용자")
	void validateUser_Failure_UnauthorizedUser() {
		// given
		Long userId = 4L; // initialSenderId나 initialReceiverId와 다름
		Long chatroomId = 1L;

		// 실제 ChatRoom 객체 생성
		ChatRoom chatRoom = ChatRoom.builder()
			.id(chatroomId)
			.postId(10L)
			.initialSenderId(2L)
			.initialReceiverId(3L)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.createdTime(LocalDateTime.now())
			.lastModifiedTime(LocalDateTime.now())
			.build();

		when(chatRoomFinder.findByChatRoomId(chatroomId))
			.thenReturn(chatRoom);

		// when & then
		assertThatThrownBy(() -> target.validateUser(userId, chatroomId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_NOT_ENTERED.getMessage());
	}
}