package com.cotato.kampus.domain.chat.implement.chatroom;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatRoomJpaRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDeleter {
	private final ChatRoomJpaRepository chatRoomJpaRepository;

	@Transactional
	public void deleteById(Long chatroomId) {
		chatRoomJpaRepository.deleteById(chatroomId);
	}
}