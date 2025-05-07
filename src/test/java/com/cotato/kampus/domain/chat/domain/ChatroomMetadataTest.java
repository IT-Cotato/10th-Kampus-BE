package com.cotato.kampus.domain.chat.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cotato.kampus.domain.chat.enums.ChatType;

class ChatroomMetadataTest {

	@Test
	@DisplayName("채팅방 메타데이터 생성 성공")
	public void createChatroomMetadata() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;
		Long postId = 3L;
		String postTitle = "Test Post Title";

		// when
		ChatroomMetadata metadata = ChatroomMetadata.create(chatroomId, ChatType.POST, senderId, postId, postTitle);

		// then
		assertEquals(chatroomId, metadata.getChatroomId());
		assertEquals(senderId, metadata.getUserId());
		assertEquals(postId, metadata.getPostId());
		assertEquals(postTitle, metadata.getPostTitle());
		assertEquals(0L, metadata.getLastMessageId());
		assertFalse(metadata.isLastMessageImage());
		assertEquals("", metadata.getLastMessageContent());
		assertNotNull(metadata.getLastChatTime());
		assertEquals(0L, metadata.getUnreadCount());
	}

	@Test
	@DisplayName("채팅방 메타데이터 업데이트 성공")
	public void updateChatroomMetadata() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;
		Long postId = 3L;
		String postTitle = "Test Post Title";
		ChatroomMetadata metadata = ChatroomMetadata.create(chatroomId, ChatType.POST, senderId, postId, postTitle);

		Long messageId = 4L;
		boolean isImage = true;
		String content = "Test Message Content";
		LocalDateTime chatTime = LocalDateTime.now();

		// when
		metadata.updateLastMessage(messageId, isImage, content, chatTime);

		// then
		assertEquals(messageId, metadata.getLastMessageId());
		assertTrue(metadata.isLastMessageImage());
		assertEquals(content, metadata.getLastMessageContent());
		assertEquals(chatTime, metadata.getLastChatTime());
	}

	@Test
	@DisplayName("채팅방 메타데이터 읽지 않은 메시지 수 증가 성공")
	public void incrementUnreadCount() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;
		Long postId = 3L;
		String postTitle = "Test Post Title";
		ChatroomMetadata metadata = ChatroomMetadata.create(chatroomId, ChatType.POST, senderId, postId, postTitle);

		// when
		metadata.incrementUnreadCount();

		// then
		assertEquals(1L, metadata.getUnreadCount());
	}

	@Test
	@DisplayName("채팅방 메타데이터 읽지 않은 메시지 수 초기화 성공")
	public void resetUnreadCount() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;
		Long postId = 3L;
		String postTitle = "Test Post Title";
		ChatroomMetadata metadata = ChatroomMetadata.create(chatroomId, ChatType.POST, senderId, postId, postTitle);

		// when
		metadata.incrementUnreadCount();
		metadata.resetUnreadCount();

		// then
		assertEquals(0L, metadata.getUnreadCount());
	}
}