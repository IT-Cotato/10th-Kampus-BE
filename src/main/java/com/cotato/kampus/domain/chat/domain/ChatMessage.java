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
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Column(name = "content", nullable = false, length = 500)
	private String content;

	@Builder
	public ChatMessage(Long chatroomId, Long senderId, String content) {
		this.chatroomId = chatroomId;
		this.senderId = senderId;
		this.content = content;
	}
}