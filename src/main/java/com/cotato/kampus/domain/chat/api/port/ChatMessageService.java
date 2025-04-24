package com.cotato.kampus.domain.chat.api.port;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatNotificationResult;
import com.cotato.kampus.global.error.exception.ImageException;

public interface ChatMessageService {

	ChatNotificationResult processNewMessage(Long chatroomId, String message);

	ChatMessageSliceSnapshot getMessages(int page, Long chatroomId);

	void markMessagesAsRead(Long chatroomId);

	List<String> uploadImage(Long chatroomId, List<MultipartFile> images) throws ImageException;
}