package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatroomMetadataRepository;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataUpdater {

	private final ChatroomMetadataRepository chatroomMetadataRepository;

	@Transactional
	public void updateSenderMetadata(Long chatroomId, ChatMessage message, Long senderId) {
		ChatroomMetadata senderMetadata = chatroomMetadataRepository
			.findByChatroomIdAndUserId(chatroomId, senderId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		senderMetadata.updateLastMessage(
			message.getId(),
			message.getContent(),
			message.getCreatedTime()
		);

		chatroomMetadataRepository.save(senderMetadata);
	}

	@Transactional
	public ChatroomMetadata updateReceiverMetadata(Long chatroomId, ChatMessage message, Long receiverId) {
		ChatroomMetadata receiverMetadata = chatroomMetadataRepository
			.findByChatroomIdAndUserId(chatroomId, receiverId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		receiverMetadata.updateLastMessage(
			message.getId(),
			message.getContent(),
			message.getCreatedTime()
		);
		receiverMetadata.incrementUnreadCount();

		return chatroomMetadataRepository.save(receiverMetadata);
	}

	@Transactional
	public void resetReadCount(Long chatroomId, Long userId) {
		ChatroomMetadata metadata = chatroomMetadataRepository
			.findByChatroomIdAndUserId(chatroomId, userId)
			.orElseThrow(() -> new AppException(ErrorCode.CHATROOM_METADATA_NOT_FOUND));

		metadata.resetUnreadCount();
		chatroomMetadataRepository.save(metadata);
	}
}