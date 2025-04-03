package com.cotato.kampus.domain.chat.api.port;

public interface ChatMessageReadService {

	public void markMessagesAsRead(Long chatroomId);
}
