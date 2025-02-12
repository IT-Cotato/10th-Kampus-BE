package com.cotato.kampus.domain.chat.application;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatroomMetadataRepository;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataFinder {

	private static final int PAGE_SIZE = 20;
	private static final String SORT_PROPERTY = "lastChatTime";

	private final ChatroomMetadataRepository chatroomMetadataRepository;

	public Slice<ChatroomMetadata> findChatRoomMetadatas(Long userId, int page) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		return chatroomMetadataRepository.findAllByUserIdOrderByLastChatTimeDesc(
			userId,
			customPageRequest.of(SORT_PROPERTY)
		);
	}
}