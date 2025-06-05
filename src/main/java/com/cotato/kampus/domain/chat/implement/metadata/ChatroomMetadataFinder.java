package com.cotato.kampus.domain.chat.implement.metadata;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;
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

	public Slice<ChatroomMetadata> findChatRoomMetadatas(Long userId, int page, ChatType chatType) {
		CustomPageRequest customPageRequest = new CustomPageRequest(page, PAGE_SIZE, Sort.Direction.DESC);
		if (chatType == null) {
			return chatroomMetadataRepository.findAllByUserId(userId,
				customPageRequest.of(SORT_PROPERTY));
		}
		return chatroomMetadataRepository.findAllByUserIdAndChatType(userId, chatType,
			customPageRequest.of(SORT_PROPERTY));
	}
}