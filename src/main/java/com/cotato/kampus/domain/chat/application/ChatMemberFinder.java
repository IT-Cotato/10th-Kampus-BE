package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.Chatroom;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMemberFinder {

	private final ChatRoomFinder chatRoomFinder;

	// 채팅방의 채팅상대의 id를 찾음
	public Long findReceiverId(Long chatroomId, Long senderId) {
		Chatroom chatroom = chatRoomFinder.findChatroom(chatroomId);

		// 발신자가 초기 발신자면 초기 수신자가 상대방, 아니면 초기 발신자가 상대방
		return chatroom.getInitialSenderId().equals(senderId)
			? chatroom.getInitialReceiverId()
			: chatroom.getInitialSenderId();
	}
}