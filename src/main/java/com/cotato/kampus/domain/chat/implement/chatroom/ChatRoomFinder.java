package com.cotato.kampus.domain.chat.implement.chatroom;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomFinder {

	private final ChatRoomRepository chatRoomRepository;
	private static final int PAGE_SIZE = 10;
	private static final String SORT_PROPERTY = "createdTime";
	
	public boolean existsByReferenceIdAndSenderIdAndChatType(Long referenceId, Long senderId, ChatType chatType) {
		return chatRoomRepository.existsByReferenceIdAndInitialSenderIdAndChatType(referenceId, senderId, chatType);
	}

	public ChatRoom findByChatRoomId(Long chatroomId) {
		return chatRoomRepository.findById(chatroomId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_NOT_FOUND));
	}

	public ChatRoom findByReferenceIdAndSenderIdAndChatType(Long referenceId, Long senderId, ChatType chatType) {
		return chatRoomRepository.findByReferenceIdAndInitialSenderIdAndChatType(referenceId, senderId, chatType)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_NOT_FOUND));
	}

	public Slice<ChatRoom> findChatRooms(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return chatRoomRepository.findAllByUserIdOrderByCreatedTimeDesc(
			userId,
			customPageRequest.of(SORT_PROPERTY)
		);
	}
}