package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.MessageReadStatusRepository;
import com.cotato.kampus.domain.chat.domain.MessageReadStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusUpdater {
	private final MessageReadStatusRepository messageReadStatusRepository;

	@Transactional
	public void updateStatus(Long chatroomId, Long userId, Long latestMessageId) {
		// 읽음 상태가 없으면 새로 생성, 있으면 업데이트
		MessageReadStatus messageReadStatus = messageReadStatusRepository
			.findByChatroomIdAndUserId(chatroomId, userId)
			.orElse(MessageReadStatus.builder()
				.chatroomId(chatroomId)
				.userId(userId)
				.lastReadMessageId(latestMessageId)
				.build());

		messageReadStatus.updateLastReadMessage(latestMessageId);
		messageReadStatusRepository.save(messageReadStatus);
	}
}