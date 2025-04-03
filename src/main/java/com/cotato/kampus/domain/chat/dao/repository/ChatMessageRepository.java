package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

	Slice<ChatMessageEntity> findAllByChatroomIdOrderByCreatedTimeDesc(Long chatRoomId, PageRequest pageRequest);

	Optional<ChatMessageEntity> findFirstByChatroomIdOrderByCreatedTimeDesc(Long chatroomId);

	@Query("SELECT COUNT(m) FROM ChatMessageEntity m WHERE m.chatroomId = :chatroomId AND m.id > :messageId")
	Long countByChatroomIdAndIdGreaterThan(
		@Param("chatroomId") Long chatroomId,
		@Param("messageId") Long messageId
	);

	void deleteAllByChatroomId(Long chatroomId);
}