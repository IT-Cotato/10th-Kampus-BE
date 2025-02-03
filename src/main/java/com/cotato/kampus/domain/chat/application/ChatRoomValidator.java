package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomValidator {

	private final ChatRoomFinder chatRoomFinder;

	public void validateNewChatRoom(Long postId, Long senderId, Long receiverId) {
		validateSender(senderId, receiverId);
		validateDuplicateChatRoom(postId, senderId);
	}

	// 채팅을 거는 유저와 받는 유저가 달라야 함
	private void validateSender(Long senderId, Long receiverId) {
		if (senderId.equals(receiverId)) {
			throw new AppException(ErrorCode.INVALID_CHATROOM);
		}
	}

	// 채팅방이 이미 존재하는 경우
	private void validateDuplicateChatRoom(Long postId, Long senderId) {
		if (chatRoomFinder.existsByPostIdAndSenderId(postId, senderId)) {
			throw new AppException(ErrorCode.CHATROOM_DUPLICATED);
		}
	}
}