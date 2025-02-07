package com.cotato.kampus.domain.chat.dto;

import java.util.List;

public record ChatRoomPreviewList(
	List<ChatRoomPreview> chatRoomPreviewList,
	boolean hasNext
) {
	public static ChatRoomPreviewList from(List<ChatRoomPreview> previewList, boolean hasNext) {
		return new ChatRoomPreviewList(previewList, hasNext);
	}
}