package com.cotato.kampus.domain.chat.dao.entity;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
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
public class ChatroomMetadataEntity extends BaseTimeEntity {

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
	public ChatroomMetadataEntity(Long id, Long chatroomId, Long userId, Long postId, String postTitle,
		Long lastMessageId, String lastMessageContent, LocalDateTime lastChatTime, Long unreadCount) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.postId = postId;
		this.postTitle = postTitle;
		this.lastMessageId = lastMessageId;
		this.lastMessageContent = lastMessageContent;
		this.lastChatTime = lastChatTime;
		this.unreadCount = unreadCount;
	}

	public static ChatroomMetadata toDomain(ChatroomMetadataEntity chatroomMetadataEntity) {
		return ChatroomMetadata.builder()
			.id(chatroomMetadataEntity.getId())
			.chatroomId(chatroomMetadataEntity.getChatroomId())
			.userId(chatroomMetadataEntity.getUserId())
			.postId(chatroomMetadataEntity.getPostId())
			.postTitle(chatroomMetadataEntity.getPostTitle())
			.lastMessageId(chatroomMetadataEntity.getLastMessageId())
			.lastMessageContent(chatroomMetadataEntity.getLastMessageContent())
			.lastChatTime(chatroomMetadataEntity.getLastChatTime())
			.unreadCount(chatroomMetadataEntity.getUnreadCount())
			.build();
	}

	public static ChatroomMetadataEntity fromDomain(ChatroomMetadata chatroomMetadata) {
		return ChatroomMetadataEntity.builder()
			.id(chatroomMetadata.getId())
			.chatroomId(chatroomMetadata.getChatroomId())
			.userId(chatroomMetadata.getUserId())
			.postId(chatroomMetadata.getPostId())
			.postTitle(chatroomMetadata.getPostTitle())
			.lastMessageId(chatroomMetadata.getLastMessageId())
			.lastMessageContent(chatroomMetadata.getLastMessageContent())
			.lastChatTime(chatroomMetadata.getLastChatTime())
			.unreadCount(chatroomMetadata.getUnreadCount())
			.build();
	}
}