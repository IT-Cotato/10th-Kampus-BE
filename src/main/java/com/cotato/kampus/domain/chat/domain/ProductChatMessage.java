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
@Table(name = "product_chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductChatMessage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_chat_message_id")
	private Long id;

	@Column(name = "message_id", nullable = false)
	private Long messageId;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Builder
	public ProductChatMessage(Long messageId, Long chatroomId, Long productId, Long userId) {
		this.messageId = messageId;
		this.chatroomId = chatroomId;
		this.productId = productId;
		this.userId = userId;
	}
}
