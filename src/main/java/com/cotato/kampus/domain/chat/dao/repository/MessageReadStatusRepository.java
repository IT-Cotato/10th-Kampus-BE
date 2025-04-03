package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.MessageReadStatus;

@Repository
public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatus, Long> {
	Optional<MessageReadStatus> findByChatroomIdAndUserId(Long chatroomId, Long userId);

	void deleteAllByChatroomId(Long chatroomId);
}