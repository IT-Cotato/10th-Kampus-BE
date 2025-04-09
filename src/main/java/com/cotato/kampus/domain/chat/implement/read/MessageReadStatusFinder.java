package com.cotato.kampus.domain.chat.implement.read;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;
import com.cotato.kampus.domain.chat.implement.read.port.MessageReadStatusRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusFinder {

	private final MessageReadStatusRepository messageReadStatusRepository;

	public Optional<MessageReadStatus> findByChatroomIdAndUserId(Long chatroomId, Long userId) {
		return messageReadStatusRepository.findByChatroomIdAndUserId(chatroomId, userId);
	}
}