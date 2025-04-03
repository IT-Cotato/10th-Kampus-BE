package com.cotato.kampus.domain.chat.implement.message;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatMessageJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.implement.message.port.ChatMessageRepository;

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
			.build();
		return chatMessageRepository.save(chatMessage);
	}
}