package com.cotato.kampus.domain.chat.domain;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessage;

public record ChatMessageSlice(
	Slice<ChatMessage> chatMessages
) {
	public static ChatMessageSlice from(Slice<ChatMessage> chatMessages) {
		return new ChatMessageSlice(chatMessages);
	}
}