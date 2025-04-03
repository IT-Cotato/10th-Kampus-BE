package com.cotato.kampus.domain.chat.implement.message;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatMessageRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDeleter {
	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public void deleteByChatroomId(Long chatroomId) {
		chatMessageRepository.deleteAllByChatroomId(chatroomId);
	}
}