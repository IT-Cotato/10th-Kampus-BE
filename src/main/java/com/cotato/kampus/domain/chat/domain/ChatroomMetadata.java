package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.enums.ChatType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ChatroomMetadata {

	private Long id;
	private Long chatroomId;
	private ChatType chatType;
	private Long userId;
	private Long referenceId;
	private String title;
	private Long lastMessageId;
	private boolean isLastMessageImage;
	private String lastMessageContent;
	private LocalDateTime lastChatTime;
	private Long unreadCount;

	@Builder
	private ChatroomMetadata(Long id, Long chatroomId, ChatType chatType, Long userId, Long referenceId,
		String title, boolean isLastMessageImage, Long lastMessageId, String lastMessageContent,
		LocalDateTime lastChatTime, Long unreadCount) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.chatType = chatType;
		this.userId = userId;
		this.referenceId = referenceId;
		this.title = title;
		this.lastMessageId = lastMessageId;
		this.isLastMessageImage = isLastMessageImage;
		this.lastMessageContent = lastMessageContent;
		this.lastChatTime = lastChatTime;
		this.unreadCount = unreadCount != null ? unreadCount : 0L;
	}

	public static ChatroomMetadata create(Long chatroomId, ChatType chatType, Long userId, Long referenceId,
		String title) {
		return ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.chatType(chatType)
			.userId(userId)
			.referenceId(referenceId)
			.title(title)
			.lastMessageId(0L)
			.isLastMessageImage(false)
			.lastMessageContent("")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(0L)
			.build();
	}

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
