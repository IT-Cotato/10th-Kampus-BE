package com.cotato.kampus.domain.chat.implement.metadata;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataAppender {

	private final ChatroomMetadataRepository chatroomMetadataRepository;

	@Transactional
	public void createMetadataPair(Long chatroomId, ChatType chatType, Long referenceId, String title,
		Long senderId, Long receiverId) {
		// 발신자 메타데이터
		ChatroomMetadata senderMetadata = ChatroomMetadata.create(
			chatroomId,
			chatType,
			senderId,
			referenceId,
			title
		);

		// 수신자 메타데이터
		ChatroomMetadata receiverMetadata = ChatroomMetadata.create(
			chatroomId,
			chatType,
			receiverId,
			referenceId,
			title
		);

		chatroomMetadataRepository.saveAll(List.of(senderMetadata, receiverMetadata));
	}
}