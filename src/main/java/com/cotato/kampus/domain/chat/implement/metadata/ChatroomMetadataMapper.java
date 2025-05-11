package com.cotato.kampus.domain.chat.implement.metadata;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataMapper {

	public ChatRoomPreview toChatRoomPreview(ChatroomMetadata metadata) {
		return ChatRoomPreview.of(
			metadata.getChatroomId(),
			metadata.getTitle(),
			metadata.getLastMessageId(),
			metadata.getLastMessageContent(),
			metadata.getLastChatTime(),
			metadata.getUnreadCount());
	}
}