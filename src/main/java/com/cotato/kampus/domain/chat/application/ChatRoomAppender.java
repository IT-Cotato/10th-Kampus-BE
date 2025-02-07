package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatRoomRepository;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomAppender {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public Long appendChatRoom(Long postId, Long senderId, Long receiverId) {
		Chatroom chatroom = Chatroom.builder()
			.postId(postId)
			.initialSenderId(senderId)
			.initialReceiverId(receiverId)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();
		return chatRoomRepository.save(chatroom).getId();
	}
}