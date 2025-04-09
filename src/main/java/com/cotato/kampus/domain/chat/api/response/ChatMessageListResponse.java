package com.cotato.kampus.domain.chat.api.response;

import java.util.List;

import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;

public record ChatMessageListResponse(
	List<ChatMessageResponse> messages,
	boolean hasNext
) {
	public static ChatMessageListResponse from(ChatMessageSliceSnapshot sliceSnapshot) {
		List<ChatMessageResponse> responseMessages = sliceSnapshot.messages()
			.stream()
			.map(ChatMessageResponse::from)
			.toList();

		return new ChatMessageListResponse(
			responseMessages,
			sliceSnapshot.hasNext()
		);
	}
}