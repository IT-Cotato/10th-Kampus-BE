package com.cotato.kampus.domain.chat.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

class ChatRoomTest {

	@Test
	@DisplayName("채팅방 생성 성공")
	void create() {
		// given
		ChatRoom chatRoom = ChatRoom.create(1L, 2L, 3L);
		// when
		// then
		assertThat(chatRoom.getPostId()).isEqualTo(1L);
		assertThat(chatRoom.getInitialSenderId()).isEqualTo(2L);
		assertThat(chatRoom.getInitialReceiverId()).isEqualTo(3L);
		assertThat(chatRoom.getIsBlocked()).isFalse();
		assertThat(chatRoom.getInitiatedFrom()).isEqualTo(InitiatedFrom.POST);
	}

	@Test
	@DisplayName("채팅방 생성 실패 - senderId와 receiverId가 같을 때")
	void createFail() {
		// given
		Long senderId = 1L;
		Long receiverId = 1L;
		// when
		// then
		assertThatThrownBy(() -> ChatRoom.create(1L, senderId, receiverId))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.INVALID_CHATROOM.getMessage());
	}

	@Test
	@DisplayName("채팅방에 들어가 있지 않은 유저가 조회하는 경우")
	void validateEnteredUserFail() {
		// given
		Long userId = 1L;
		Long chatRoomId = 1L;
		ChatRoom chatRoom = ChatRoom.create(1L, 2L, 3L);
		// when
		// then
		assertThatThrownBy(() -> chatRoom.validateEnteredUser(userId))
			.isInstanceOf(AppException.class)
			.hasMessageContaining(ErrorCode.CHATROOM_NOT_ENTERED.getMessage());
	}
}