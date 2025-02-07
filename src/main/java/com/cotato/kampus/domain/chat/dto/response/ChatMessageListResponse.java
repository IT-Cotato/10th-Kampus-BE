package com.cotato.kampus.domain.chat.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.cotato.kampus.domain.chat.dto.ChatMessageSliceWithUserId;

public record ChatMessageListResponse(
	Long currentUserId,
	List<ChatMessageResponse> messages,
	boolean hasNext
) {
	public static ChatMessageListResponse from(ChatMessageSliceWithUserId sliceWithUserId) {
		Long currentUserId = sliceWithUserId.currentUserId();
		List<ChatMessageResponse> responseMessages = sliceWithUserId.messages()
			.chatMessages()
			.stream()
			.map(chatMessage -> ChatMessageResponse.from(chatMessage, currentUserId))
			.collect(Collectors.toList());
		return new ChatMessageListResponse(
			currentUserId,
			responseMessages,
			sliceWithUserId.messages().chatMessages().hasNext()
		);
	}
}