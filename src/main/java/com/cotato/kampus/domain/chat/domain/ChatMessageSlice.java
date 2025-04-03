package com.cotato.kampus.domain.chat.domain;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;

public record ChatMessageSlice(
	Slice<ChatMessageEntity> chatMessages
) {
	public static ChatMessageSlice from(Slice<ChatMessageEntity> chatMessages) {
		return new ChatMessageSlice(chatMessages);
	}
}