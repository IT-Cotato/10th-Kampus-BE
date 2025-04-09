package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.MessageReadStatusEntity;
import com.cotato.kampus.domain.chat.domain.MessageReadStatus;
import com.cotato.kampus.domain.chat.implement.read.port.MessageReadStatusRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MessageReadStatusRepositoryImpl implements MessageReadStatusRepository {

	private final MessageReadStatusJpaRepository messageReadStatusJpaRepository;

	@Override
	public void save(MessageReadStatus messageReadStatus) {
		messageReadStatusJpaRepository.save(
			MessageReadStatusEntity.fromDomain(messageReadStatus)
		);
	}

	@Override
	public Optional<MessageReadStatus> findByChatroomIdAndUserId(Long chatroomId, Long userId) {
		return messageReadStatusJpaRepository.findByChatroomIdAndUserId(chatroomId, userId)
			.map(MessageReadStatusEntity::toDomain);
	}

	@Override
	public void deleteAllByChatroomId(Long chatroomId) {
		messageReadStatusJpaRepository.deleteAllByChatroomId(chatroomId);
	}
}
