package com.cotato.kampus.domain.chat.domain;

import com.cotato.kampus.domain.common.domain.BaseTimeEntity;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;

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
public class Chatroom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chatroom_id")
	private Long id;

	@Column(name = "post_id", nullable = false)
	private Long postId;

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
	public Chatroom(Long postId, Long initialSenderId, Long initialReceiverId,
		Boolean isBlocked, InitiatedFrom initiatedFrom) {
		this.postId = postId;
		this.initialSenderId = initialSenderId;
		this.initialReceiverId = initialReceiverId;
		this.isBlocked = isBlocked;
		this.initiatedFrom = initiatedFrom;
	}
}
