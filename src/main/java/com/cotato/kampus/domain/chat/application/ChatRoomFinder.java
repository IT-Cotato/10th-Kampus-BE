package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatRoomRepository;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.dto.ChatRoomDto;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFinder {

	private final ChatRoomRepository chatRoomRepository;

	public boolean existsByPostIdAndSenderId(Long postId, Long senderId) {
		return chatRoomRepository.existsByPostIdAndInitialSenderId(postId, senderId);
	}

	public ChatRoomDto findByChatRoomId(Long chatroomId) {
		Chatroom chatroom = chatRoomRepository.findById(chatroomId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_NOT_FOUND));
		return ChatRoomDto.from(chatroom);
	}
}