package com.cotato.kampus.domain.chat.application;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.MessageReadStatusRepository;
import com.cotato.kampus.domain.chat.dto.MessageReadStatusDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusFinder {

	private final MessageReadStatusRepository messageReadStatusRepository;

	public Optional<MessageReadStatusDto> findByChatroomIdAndUserId(Long chatroomId, Long userId) {
		return messageReadStatusRepository.findByChatroomIdAndUserId(chatroomId, userId)
			.map(MessageReadStatusDto::from);
	}
}