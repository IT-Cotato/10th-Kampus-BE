package com.cotato.kampus.domain.chat.implement.metadata;

import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadataMapper {

	public ChatRoomPreview toChatRoomPreview(ChatroomMetadataEntity metadata) {
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