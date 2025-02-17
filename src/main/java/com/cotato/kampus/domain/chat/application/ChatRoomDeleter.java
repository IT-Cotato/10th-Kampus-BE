package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatRoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDeleter {
	private final ChatRoomRepository chatRoomRepository;

	@Transactional
	public void deleteById(Long chatroomId) {
		chatRoomRepository.deleteById(chatroomId);
	}
}