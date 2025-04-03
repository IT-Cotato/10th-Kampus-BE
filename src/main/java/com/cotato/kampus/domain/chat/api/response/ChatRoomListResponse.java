package com.cotato.kampus.domain.chat.api.response;

import java.util.List;

import com.cotato.kampus.domain.chat.domain.ChatRoomPreview;
import com.cotato.kampus.domain.chat.domain.ChatRoomPreviewList;

public record ChatRoomListResponse(
	List<ChatRoomPreview> chatRoomPreviewList,
	boolean hasNext
) {
	public static ChatRoomListResponse from(ChatRoomPreviewList chatRooms) {
		return new ChatRoomListResponse(chatRooms.chatRoomPreviewList(), chatRooms.hasNext());
	}
}