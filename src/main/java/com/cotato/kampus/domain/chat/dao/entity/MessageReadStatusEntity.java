package com.cotato.kampus.domain.chat.dao.entity;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_read_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_read_status_id")
	private Long id;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Enumerated(EnumType.STRING)
	@Column(name = "chat_type", nullable = false)
	private ChatType chatType;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "last_read_message_id", nullable = false)
	private Long lastReadMessageId;

	@Builder
	public MessageReadStatusEntity(Long id, Long chatroomId, ChatType chatType, Long userId, Long lastReadMessageId) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.chatType = chatType;
		this.userId = userId;
		this.lastReadMessageId = lastReadMessageId;
	}

	public static MessageReadStatus toDomain(MessageReadStatusEntity entity) {
		return MessageReadStatus.builder()
			.id(entity.getId())
			.chatroomId(entity.getChatroomId())
			.chatType(entity.getChatType())
			.userId(entity.getUserId())
			.lastReadMessageId(entity.getLastReadMessageId())
			.createdTime(entity.getCreatedTime())
			.lastModifiedTime(entity.getLastModifiedTime())
			.build();
	}

	public static MessageReadStatusEntity fromDomain(MessageReadStatus messageReadStatus) {
		return MessageReadStatusEntity.builder()
			.id(messageReadStatus.getId())
			.chatroomId(messageReadStatus.getChatroomId())
			.chatType(messageReadStatus.getChatType())
			.userId(messageReadStatus.getUserId())
			.lastReadMessageId(messageReadStatus.getLastReadMessageId())
			.build();
	}
}