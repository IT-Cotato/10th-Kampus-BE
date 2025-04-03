package com.cotato.kampus.domain.chat.implement.read;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.repository.MessageReadStatusJpaRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusDeleter {
	private final MessageReadStatusJpaRepository messageReadStatusJpaRepository;

	@Transactional
	public void deleteByChatroomId(Long chatroomId) {
		messageReadStatusJpaRepository.deleteAllByChatroomId(chatroomId);
	}
}