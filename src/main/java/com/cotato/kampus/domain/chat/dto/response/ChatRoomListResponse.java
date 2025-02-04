package com.cotato.kampus.domain.chat.dto.response;

import java.util.List;

import com.cotato.kampus.domain.chat.dto.ChatRoomPreview;
import com.cotato.kampus.domain.chat.dto.ChatRoomPreviewList;

public record ChatRoomListResponse(
	List<ChatRoomPreview> chatRoomPreviewList,
	boolean hasNext
) {
	public static ChatRoomListResponse from(ChatRoomPreviewList chatRooms) {
		return new ChatRoomListResponse(chatRooms.chatRoomPreviewList(), chatRooms.hasNext());
	}
}