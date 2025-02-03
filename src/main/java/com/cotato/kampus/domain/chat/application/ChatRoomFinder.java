package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatRoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFinder {

	private final ChatRoomRepository chatRoomRepository;

	public boolean existsByPostIdAndSenderId(Long postId, Long senderId) {
		return chatRoomRepository.existsByPostIdAndSenderId(postId, senderId);
	}
}