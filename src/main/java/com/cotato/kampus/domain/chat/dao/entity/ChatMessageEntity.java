package com.cotato.kampus.domain.chat.dao.entity;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
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
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long id;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Enumerated(EnumType.STRING)
	@Column(name = "chat_type", nullable = false)
	private ChatType chatType;

	@Column(name = "sender_id", nullable = false)
	private Long senderId;

	@Column(name = "content", nullable = false, length = 500)
	private String content;

	@Column(name = "is_image", nullable = false)
	private boolean isImage;

	@Builder
	public ChatMessageEntity(Long chatroomId, ChatType chatType, Long senderId, String content, boolean isImage) {
		this.chatroomId = chatroomId;
		this.chatType = chatType;
		this.senderId = senderId;
		this.content = content;
		this.isImage = isImage;
	}

	public static ChatMessageEntity fromDomain(ChatMessage chatMessage) {
		return ChatMessageEntity.builder()
			.chatroomId(chatMessage.getChatroomId())
			.chatType(chatMessage.getChatType())
			.senderId(chatMessage.getSenderId())
			.content(chatMessage.getContent())
			.isImage(chatMessage.isImage())
			.build();
	}

	public ChatMessage toDomain() {
		return ChatMessage.builder()
			.id(id)
			.chatroomId(chatroomId)
			.chatType(chatType)
			.senderId(senderId)
			.content(content)
			.isImage(isImage)
			.createdTime(getCreatedTime())
			.lastModifiedTime(getLastModifiedTime())
			.build();
	}
}