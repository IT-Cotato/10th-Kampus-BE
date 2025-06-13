package com.cotato.kampus.domain.chat.application;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatroomMetadataFactory {

	public static ChatroomMetadata create(ChatType chatType) {
		return ChatroomMetadata.builder()
			.id(1L)
			.chatroomId(1L)
			.chatType(chatType)
			.userId(1L)
			.referenceId(1L)
			.title("dummy title 1")
			.lastMessageId(1L)
			.isLastMessageImage(false)
			.lastMessageContent("dummy message 1")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(0L)
			.build();
	}

	public static List<ChatroomMetadata> createList(ChatType... chatTypes) {
		List<ChatroomMetadata> list = new ArrayList<>();
		if (chatTypes.length > 0) {
			list.add(ChatroomMetadata.builder()
				.id(1L)
				.chatroomId(1L)
				.chatType(chatTypes[0])
				.userId(1L)
				.referenceId(1L)
				.title("dummy title 1")
				.lastMessageId(1L)
				.isLastMessageImage(false)
				.lastMessageContent("dummy message 1")
				.lastChatTime(LocalDateTime.now())
				.unreadCount(0L)
				.build());
		}
		if (chatTypes.length > 1) {
			list.add(ChatroomMetadata.builder()
				.id(2L)
				.chatroomId(2L)
				.chatType(chatTypes[1])
				.userId(1L)
				.referenceId(1L)
				.title("dummy title 2")
				.lastMessageId(2L)
				.isLastMessageImage(false)
				.lastMessageContent("dummy message 2")
				.lastChatTime(LocalDateTime.now())
				.unreadCount(0L)
				.build());
		}
		if (chatTypes.length > 2) {
			list.add(ChatroomMetadata.builder()
				.id(3L)
				.chatroomId(3L)
				.chatType(chatTypes[2])
				.userId(1L)
				.referenceId(1L)
				.title("dummy title 3")
				.lastMessageId(3L)
				.isLastMessageImage(false)
				.lastMessageContent("dummy message 3")
				.lastChatTime(LocalDateTime.now())
				.unreadCount(0L)
				.build());
		}
		return list;
	}
} 