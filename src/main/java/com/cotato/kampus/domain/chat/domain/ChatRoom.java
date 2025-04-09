package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {
	private final Long id;
	private final Long postId;
	private final Long initialSenderId;
	private final Long initialReceiverId;
	private final Boolean isBlocked;
	private final InitiatedFrom initiatedFrom;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	public static ChatRoom create(Long postId, Long initialSenderId, Long initialReceiverId) {
		validateSender(initialSenderId, initialReceiverId);
		return ChatRoom.builder()
			.postId(postId)
			.initialSenderId(initialSenderId)
			.initialReceiverId(initialReceiverId)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();
	}

	// 채팅을 거는 유저와 받는 유저가 달라야 함
	private static void validateSender(Long senderId, Long receiverId) {
		if (senderId.equals(receiverId)) {
			throw new AppException(ErrorCode.INVALID_CHATROOM);
		}
	}

	public void validateEnteredUser(Long userId) {
		if (!initialSenderId.equals(userId) && !initialReceiverId.equals(userId)) {
			throw new AppException(ErrorCode.CHATROOM_NOT_ENTERED);
		}
	}
}