package com.cotato.kampus.domain.chat.implement.read.port;

import java.util.Optional;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;

public interface MessageReadStatusRepository {

	void save(MessageReadStatus messageReadStatus);

	Optional<MessageReadStatus> findByChatroomIdAndUserId(Long chatroomId, Long userId);

	void deleteAllByChatroomId(Long chatroomId);
}