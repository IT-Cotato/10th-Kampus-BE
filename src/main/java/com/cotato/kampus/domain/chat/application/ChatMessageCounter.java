package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.ChatMessageRepository;
import com.cotato.kampus.domain.chat.dao.MessageReadStatusRepository;
import com.cotato.kampus.domain.chat.domain.MessageReadStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageCounter {

	private final ChatMessageRepository chatMessageRepository;
	private final MessageReadStatusRepository readStatusRepository;

	public Long countUnreadMessages(Long chatroomId, Long userId) {
		// 사용자의 마지막으로 읽은 메시지 ID 조회
		MessageReadStatus readStatus = readStatusRepository
			.findByChatroomIdAndUserId(chatroomId, userId)
			.orElse(new MessageReadStatus(chatroomId, userId, 0L));

		// 마지막으로 읽은 메시지 이후의 메시지 수 계산
		return countMessagesAfter(
			chatroomId,
			readStatus.getLastReadMessageId()
		);
	}

	public Long countMessagesAfter(Long chatroomId, Long messageId) {
		return chatMessageRepository.countByChatroomIdAndIdGreaterThan(chatroomId, messageId);
	}
}