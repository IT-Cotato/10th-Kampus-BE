package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatroom_metadata")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetadata extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chatroom_metadata_id")
	private Long id;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "post_id", nullable = false)
	private Long postId;

	@Column(name = "post_title", nullable = false)
	private String postTitle;

	@Column(name = "last_message_id", nullable = false)
	private Long lastMessageId;

	@Column(name = "last_message_content", nullable = false)
	private String lastMessageContent;

	@Column(name = "last_chat_time", nullable = false)
	private LocalDateTime lastChatTime;

	@Column(name = "unread_count", nullable = false)
	private Long unreadCount;

	@Builder
	public ChatroomMetadata(Long chatroomId, Long userId, Long postId, String postTitle,
		Long lastMessageId, String lastMessageContent, LocalDateTime lastChatTime, Long unreadCount) {
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.postId = postId;
		this.postTitle = postTitle;
		this.lastMessageId = lastMessageId;
		this.lastMessageContent = lastMessageContent;
		this.lastChatTime = lastChatTime;
		this.unreadCount = unreadCount;
	}

	public void updateLastMessage(Long messageId, String content, LocalDateTime chatTime) {
		this.lastMessageId = messageId;
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