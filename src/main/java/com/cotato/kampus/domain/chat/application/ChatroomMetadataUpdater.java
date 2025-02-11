package com.cotato.kampus.domain.chat.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatroomMetadataRepository;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataUpdater {

	private final ChatroomMetadataRepository chatroomMetadataRepository;

	@Transactional
	public void updateMetadatasOnNewMessage(Long chatroomId, ChatMessage message, Long senderId) {
		List<ChatroomMetadata> metadatas = chatroomMetadataRepository.findAllByChatroomId(chatroomId);

		metadatas.forEach(metadata -> {
			metadata.updateLastMessage(
				message.getId(),
				message.getContent(),
				message.getCreatedTime()
			);

			if (!metadata.getUserId().equals(senderId)) {
				metadata.incrementUnreadCount();
			}
		});

		chatroomMetadataRepository.saveAll(metadatas);
	}
}