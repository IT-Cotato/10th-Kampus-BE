package com.cotato.kampus.domain.chat.domain;

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
public class MessageReadStatus extends BaseTimeEntity {
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
	public MessageReadStatus(Long chatroomId, Long userId, Long lastReadMessageId) {
		this.chatroomId = chatroomId;
		this.userId = userId;
		this.lastReadMessageId = lastReadMessageId;
	}

	public void updateLastReadMessage(Long messageId) {
		this.lastReadMessageId = messageId;
	}
}