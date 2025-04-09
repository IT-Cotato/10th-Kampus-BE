package com.cotato.kampus.domain.chat.implement.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomAppender {

	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public Long appendChatRoom(Long postId, Long senderId, Long receiverId) {
		ChatRoom chatRoom = ChatRoom.builder()
			.postId(postId)
			.initialSenderId(senderId)
			.initialReceiverId(receiverId)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();
		return chatRoomRepository.save(chatRoom);
	}
}