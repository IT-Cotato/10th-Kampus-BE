package com.cotato.kampus.domain.chat.implement.metadata;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataDeleter {
	private final ChatroomMetadataRepository chatroomMetadataRepository;

	@Transactional
	public void deleteByChatroomId(Long chatroomId) {
		chatroomMetadataRepository.deleteAllByChatroomId(chatroomId);
	}
}