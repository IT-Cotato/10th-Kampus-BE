package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatMessageRepository;
import com.cotato.kampus.domain.chat.domain.ChatMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageAppender {

	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessage appendChatMessage(Long senderId, Long chatRoomId, String content) {
		ChatMessage chatMessage = ChatMessage.builder()
			.chatroomId(chatRoomId)
			.senderId(senderId)
			.content(content)
			.isRead(false)
			.build();
		return chatMessageRepository.save(chatMessage);
	}
}