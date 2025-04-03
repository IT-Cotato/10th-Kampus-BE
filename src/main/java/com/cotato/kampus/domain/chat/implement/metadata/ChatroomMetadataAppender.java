package com.cotato.kampus.domain.chat.implement.metadata;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.ChatroomMetadataJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataAppender {

	private final ChatroomMetadataJpaRepository chatroomMetadataJpaRepository;

	@Transactional
	public void appendChatroomMetadatas(Long chatroomId, Long postId, String postTitle,
		Long senderId, Long receiverId) {
		// 발신자 메타데이터
		ChatroomMetadataEntity senderMetadata = ChatroomMetadataEntity.builder()
			.chatroomId(chatroomId)
			.userId(senderId)
			.postId(postId)
			.postTitle(postTitle)
			.lastMessageId(0L)
			.lastMessageContent("")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(0L)
			.build();

		// 수신자 메타데이터
		ChatroomMetadataEntity receiverMetadata = ChatroomMetadataEntity.builder()
			.chatroomId(chatroomId)
			.userId(receiverId)
			.postId(postId)
			.postTitle(postTitle)
			.lastMessageId(0L)
			.lastMessageContent("")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(0L)
			.build();

		chatroomMetadataJpaRepository.saveAll(List.of(senderMetadata, receiverMetadata));
	}
}