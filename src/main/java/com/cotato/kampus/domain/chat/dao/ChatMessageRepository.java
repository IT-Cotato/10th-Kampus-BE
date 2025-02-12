package com.cotato.kampus.domain.chat.dao;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	Slice<ChatMessage> findAllByChatroomIdOrderByCreatedTimeDesc(Long chatRoomId, PageRequest pageRequest);

	Optional<ChatMessage> findFirstByChatroomIdOrderByCreatedTimeDesc(Long chatroomId);

	@Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.chatroomId = :chatroomId AND m.id > :messageId")
	Long countByChatroomIdAndIdGreaterThan(
		@Param("chatroomId") Long chatroomId,
		@Param("messageId") Long messageId
	);
}