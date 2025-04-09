package com.cotato.kampus.domain.chat.api.port;

import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;

public interface ChatMessageService {

	ChatNotificationResult processNewMessage(Long chatroomId, String message);

	ChatMessageSliceSnapshot getMessages(int page, Long chatroomId);

	void markMessagesAsRead(Long chatroomId);
}