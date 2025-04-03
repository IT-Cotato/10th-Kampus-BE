package com.cotato.kampus.domain.chat.implement.message;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatMessageJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageAppender {

	private final ChatMessageJpaRepository chatMessageJpaRepository;

	@Transactional
	public ChatMessageEntity appendChatMessage(Long senderId, Long chatRoomId, String content) {
		ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
			.chatroomId(chatRoomId)
			.senderId(senderId)
			.content(content)
			.build();
		return chatMessageJpaRepository.save(chatMessageEntity);
	}
}