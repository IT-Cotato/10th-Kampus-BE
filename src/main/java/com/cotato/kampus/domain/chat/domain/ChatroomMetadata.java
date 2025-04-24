package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
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

	@Builder
	private ChatroomMetadata(Long id, Long chatroomId, Long userId, Long postId,
		String postTitle, boolean isLastMessageImage, Long lastMessageId, String lastMessageContent,
		LocalDateTime lastChatTime, Long unreadCount) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.postId = postId;
		this.postTitle = postTitle;
		this.lastMessageId = lastMessageId;
		this.isLastMessageImage = isLastMessageImage;
		this.lastMessageContent = lastMessageContent;
		this.lastChatTime = lastChatTime;
		this.unreadCount = unreadCount != null ? unreadCount : 0L;
	}

	public static ChatroomMetadata create(Long chatroomId, Long userId, Long postId,
		String postTitle, boolean isLastMessageImage) {
		return ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.userId(userId)
			.postId(postId)
			.postTitle(postTitle)
			.lastMessageId(0L)
			.isLastMessageImage(isLastMessageImage)
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
