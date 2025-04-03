package com.cotato.kampus.domain.chat.implement.read;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.MessageReadStatusJpaRepository;
import com.cotato.kampus.domain.chat.domain.MessageReadStatusDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusFinder {

	private final MessageReadStatusJpaRepository messageReadStatusJpaRepository;

	public Optional<MessageReadStatusDto> findByChatroomIdAndUserId(Long chatroomId, Long userId) {
		return messageReadStatusJpaRepository.findByChatroomIdAndUserId(chatroomId, userId)
			.map(MessageReadStatusDto::from);
	}
}