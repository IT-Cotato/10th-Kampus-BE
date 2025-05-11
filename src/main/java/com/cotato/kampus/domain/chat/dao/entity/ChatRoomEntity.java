package com.cotato.kampus.domain.chat.dao.entity;

import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
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
@Table(name = "chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chatroom_id")
	private Long id;

	@Column(name = "reference_id", nullable = false)
	private Long referenceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "chat_type", nullable = false)
	private ChatType chatType;

	@Column(name = "initial_sender_id", nullable = false)
	private Long initialSenderId;

	@Column(name = "initial_receiver_id", nullable = false)
	private Long initialReceiverId;

	@Column(name = "is_blocked")
	private Boolean isBlocked;

	@Enumerated(EnumType.STRING)
	@Column(name = "initiated_from")
	private InitiatedFrom initiatedFrom;

	@Builder
	public ChatRoomEntity(Long referenceId, ChatType chatType, Long initialSenderId, Long initialReceiverId,
		Boolean isBlocked, InitiatedFrom initiatedFrom) {
		this.referenceId = referenceId;
		this.chatType = chatType;
		this.initialSenderId = initialSenderId;
		this.initialReceiverId = initialReceiverId;
		this.isBlocked = isBlocked;
		this.initiatedFrom = initiatedFrom;
	}

	public static ChatRoom toDomain(ChatRoomEntity chatRoomEntity) {
		return ChatRoom.builder()
			.id(chatRoomEntity.getId())
			.referenceId(chatRoomEntity.getReferenceId())
			.chatType(chatRoomEntity.getChatType())
			.initialSenderId(chatRoomEntity.getInitialSenderId())
			.initialReceiverId(chatRoomEntity.getInitialReceiverId())
			.isBlocked(chatRoomEntity.getIsBlocked())
			.initiatedFrom(chatRoomEntity.getInitiatedFrom())
			.createdTime(chatRoomEntity.getCreatedTime())
			.lastModifiedTime(chatRoomEntity.getLastModifiedTime())
			.build();
	}

	public static ChatRoomEntity fromDomain(ChatRoom chatRoom) {
		return ChatRoomEntity.builder()
			.referenceId(chatRoom.getReferenceId())
			.chatType(chatRoom.getChatType())
			.initialSenderId(chatRoom.getInitialSenderId())
			.initialReceiverId(chatRoom.getInitialReceiverId())
			.isBlocked(chatRoom.getIsBlocked())
			.initiatedFrom(chatRoom.getInitiatedFrom())
			.build();
	}
}