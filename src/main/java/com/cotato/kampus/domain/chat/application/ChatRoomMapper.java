package com.cotato.kampus.domain.chat.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.Chatroom;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreview;
import com.cotato.kampus.domain.post.application.PostFinder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomMapper {
	private final PostFinder postFinder;
	private final ChatMessageFinder chatMessageFinder;

	public ChatRoomPreview toChatRoomPreview(Chatroom chatroom) {
		String title = postFinder.findPost(chatroom.getPostId()).title();
		ChatMessage lastMessage = chatMessageFinder.findLatestMessage(chatroom.getId());

		String lastMessageContent = lastMessage != null ? lastMessage.getContent() : "";
		LocalDateTime lastMessageTime = lastMessage != null ? lastMessage.getCreatedTime() : chatroom.getCreatedTime();

		return ChatRoomPreview.of(
			chatroom.getId(),
			title,
			lastMessageContent,
			lastMessageTime
		);
	}
}