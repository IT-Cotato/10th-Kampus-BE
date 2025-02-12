package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreview;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataMapper {

	public ChatRoomPreview toChatRoomPreview(ChatroomMetadata metadata) {
		return ChatRoomPreview.of(
			metadata.getChatroomId(),
			metadata.getPostTitle(),
			metadata.getLastMessageId(),
			metadata.getLastMessageContent(),
			metadata.getLastChatTime(),
			metadata.getUnreadCount()
		);
	}
}