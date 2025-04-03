package com.cotato.kampus.domain.chat.implement.metadata;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatroomMetadataJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;
import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataUpdater {

	private final ChatroomMetadataJpaRepository chatroomMetadataJpaRepository;

	@Transactional
	public void updateSenderMetadata(Long chatroomId, ChatMessageEntity message, Long senderId) {
		ChatroomMetadataEntity senderMetadata = chatroomMetadataJpaRepository
			.findByChatroomIdAndUserId(chatroomId, senderId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		senderMetadata.updateLastMessage(
			message.getId(),
			message.getContent(),
			message.getCreatedTime()
		);

		chatroomMetadataJpaRepository.save(senderMetadata);
	}

	@Transactional
	public ChatroomMetadataEntity updateReceiverMetadata(Long chatroomId, ChatMessageEntity message, Long receiverId) {
		ChatroomMetadataEntity receiverMetadata = chatroomMetadataJpaRepository
			.findByChatroomIdAndUserId(chatroomId, receiverId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		receiverMetadata.updateLastMessage(
			message.getId(),
			message.getContent(),
			message.getCreatedTime()
		);
		receiverMetadata.incrementUnreadCount();

		return chatroomMetadataJpaRepository.save(receiverMetadata);
	}

	@Transactional
	public void resetReadCount(Long chatroomId, Long userId) {
		ChatroomMetadataEntity metadata = chatroomMetadataJpaRepository
			.findByChatroomIdAndUserId(chatroomId, userId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		metadata.resetUnreadCount();
		chatroomMetadataJpaRepository.save(metadata);
	}
}