package com.cotato.kampus.domain.chat.dao.entity;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;
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

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "last_read_message_id", nullable = false)
	private Long lastReadMessageId;

	@Builder
	public MessageReadStatusEntity(Long id, Long chatroomId, Long userId, Long lastReadMessageId) {
		this.id = id;
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.lastReadMessageId = lastReadMessageId;
	}

	public static MessageReadStatus toDomain(MessageReadStatusEntity entity) {
		return MessageReadStatus.builder()
			.id(entity.getId())
			.chatroomId(entity.getChatroomId())
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
			.userId(messageReadStatus.getUserId())
			.lastReadMessageId(messageReadStatus.getLastReadMessageId())
			.build();
	}
}