package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.enums.InitiatedFrom;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoom {
	private final Long id;
	private final Long postId;
	private final Long initialSenderId;
	private final Long initialReceiverId;
	private final Boolean isBlocked;
	private final InitiatedFrom initiatedFrom;
	private final LocalDateTime createdTime;
	private final LocalDateTime lastModifiedTime;

	public void validateUser(Long userId) {
		if (!this.initialReceiverId.equals(userId) && !this.initialSenderId.equals(userId)) {
			throw new AppException(ErrorCode.CHATROOM_NOT_ENTERED);
		}
	}
}