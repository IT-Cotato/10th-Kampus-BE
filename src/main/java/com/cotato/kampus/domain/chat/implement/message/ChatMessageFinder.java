package com.cotato.kampus.domain.chat.implement.message;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatMessageRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;
import com.cotato.kampus.domain.chat.domain.ChatMessageSlice;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageFinder {

	private final ChatMessageRepository chatMessageRepository;
	private static final int PAGE_SIZE = 20;
	private static final String SORT_PROPERTY = "createdTime";

	public ChatMessageSlice findAllByChatRoomId(int page, Long chatRoomId) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		Slice<ChatMessageEntity> chatMessages = chatMessageRepository.findAllByChatroomIdOrderByCreatedTimeDesc(
			chatRoomId,
			customPageRequest.of(SORT_PROPERTY)
		);
		return ChatMessageSlice.from(chatMessages);
	}

	public ChatMessageEntity findLatestMessage(Long chatroomId) {
		return chatMessageRepository.findFirstByChatroomIdOrderByCreatedTimeDesc(chatroomId)
			.orElse(null);
	}
}
