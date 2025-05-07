package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.enums.ChatType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageReadStatus {

	private Long id;
	private Long chatroomId;
	private ChatType chatType;
	private Long userId;
	private Long lastReadMessageId;
	private LocalDateTime createdTime;
	private LocalDateTime lastModifiedTime;

	public void updateLastReadMessage(Long messageId) {
		this.lastReadMessageId = messageId;
	}
}