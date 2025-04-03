package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;

public interface ChatMessageService {

	public ChatNotificationResult processNewMessage(Long chatroomId, String message);

	public ChatMessageSliceSnapshot getMessages(int page, Long chatroomId);

	public void markMessagesAsRead(Long chatroomId);
}