package com.cotato.kampus.domain.chat.implement.chatroom;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatRoomJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.chat.domain.ChatRoomDto;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFinder {

	private final ChatRoomJpaRepository chatRoomJpaRepository;
	private static final int PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";

	public boolean existsByPostIdAndSenderId(Long postId, Long senderId) {
		return chatRoomJpaRepository.existsByPostIdAndInitialSenderId(postId, senderId);
	}

	public ChatRoomDto findByChatRoomId(Long chatroomId) {
		ChatRoomEntity chatRoomEntity = chatRoomJpaRepository.findById(chatroomId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_NOT_FOUND));
		return ChatRoomDto.from(chatRoomEntity);
	}

	public Slice<ChatRoomEntity> findChatRooms(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return chatRoomJpaRepository.findAllByUserIdOrderByCreatedTimeDesc(
			userId,
			customPageRequest.of(SORT_PROPERTY)
		);
	}

	public ChatRoomEntity findChatroom(Long chatroomId) {
		return chatRoomJpaRepository.findById(chatroomId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_NOT_FOUND));
	}
}