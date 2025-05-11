package com.cotato.kampus.domain.chat.implement.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomValidator {

	private final ChatRoomFinder chatRoomFinder;

	// 채팅방이 이미 존재하는 경우
	public void validateDuplicateChatRoom(Long referenceId, Long senderId, ChatType chatType) {
		if (chatRoomFinder.existsByReferenceIdAndSenderIdAndChatType(referenceId, senderId, chatType)) {
			throw new AppException(ErrorCode.CHATROOM_DUPLICATED);
		}
	}

	// 채팅방에 들어가있지 않은 유저가 조회하는 경우
	public void validateEnteredUser(Long userId, Long chatroomId) {
		ChatRoom chatRoom = chatRoomFinder.findByChatRoomId(chatroomId);
		chatRoom.validateEnteredUser(userId);
	}
}