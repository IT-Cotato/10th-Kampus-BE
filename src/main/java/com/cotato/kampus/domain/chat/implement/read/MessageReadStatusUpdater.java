package com.cotato.kampus.domain.chat.implement.read;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.MessageReadStatusJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.MessageReadStatusEntity;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusUpdater {
	private final MessageReadStatusJpaRepository messageReadStatusJpaRepository;

	@Transactional
	public void updateStatus(Long chatroomId, Long userId, Long latestMessageId) {
		// 읽음 상태가 없으면 새로 생성, 있으면 업데이트
		MessageReadStatusEntity messageReadStatus = messageReadStatusJpaRepository
			.findByChatroomIdAndUserId(chatroomId, userId)
			.orElse(MessageReadStatusEntity.builder()
				.chatroomId(chatroomId)
				.userId(userId)
				.lastReadMessageId(latestMessageId)
				.build());

		messageReadStatus.updateLastReadMessage(latestMessageId);
		messageReadStatusJpaRepository.save(messageReadStatus);
	}
}