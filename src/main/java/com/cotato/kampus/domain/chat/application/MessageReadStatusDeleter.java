package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.dao.MessageReadStatusRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadStatusDeleter {
	private final MessageReadStatusRepository messageReadStatusRepository;

	@Transactional
	public void deleteByChatroomId(Long chatroomId) {
		messageReadStatusRepository.deleteAllByChatroomId(chatroomId);
	}
}