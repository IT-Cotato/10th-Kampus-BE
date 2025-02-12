package com.cotato.kampus.domain.chat.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatroomMetadataRepository;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataAppender {

	private final ChatroomMetadataRepository chatroomMetadataRepository;

	@Transactional
	public void appendChatroomMetadatas(Long chatroomId, Long postId, String postTitle,
		Long senderId, Long receiverId) {
		// 발신자 메타데이터
		ChatroomMetadata senderMetadata = ChatroomMetadata.builder()
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
		ChatroomMetadata receiverMetadata = ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.userId(receiverId)
			.postId(postId)
			.postTitle(postTitle)
			.lastMessageId(0L)
			.lastMessageContent("")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(0L)
			.build();

		chatroomMetadataRepository.saveAll(List.of(senderMetadata, receiverMetadata));
	}
}