package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatroomMetadata {

	private Long id;
	private Long chatroomId;
	private Long userId;
	private Long postId;
	private String postTitle;
	private Long lastMessageId;
	private boolean isLastMessageImage;
	private String lastMessageContent;
	private LocalDateTime lastChatTime;
	private Long unreadCount;

	public void updateLastMessage(Long messageId, boolean isImage, String content, LocalDateTime chatTime) {
		this.lastMessageId = messageId;
		this.isLastMessageImage = isImage;
		this.lastMessageContent = content;
		this.lastChatTime = chatTime;
	}

	public void incrementUnreadCount() {
		this.unreadCount++;
	}

	public void resetUnreadCount() {
		this.unreadCount = 0L;
	}
}
