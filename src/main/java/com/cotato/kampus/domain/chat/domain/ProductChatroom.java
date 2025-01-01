package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.model.domain.BaseTimeEntity;

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
@Table(name = "product_chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductChatroom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_chatroom_id")
	private Long id;

	@Column(name = "chatroom_id", nullable = false)
	private Long chatroomId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "buyer_id")
	private Long buyerId;

	@Builder
	public ProductChatroom(Long chatroomId, Long productId, Long buyerId) {
		this.chatroomId = chatroomId;
		this.productId = productId;
		this.buyerId = buyerId;
	}
}
