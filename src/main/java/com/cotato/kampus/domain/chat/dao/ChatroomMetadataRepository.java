package com.cotato.kampus.domain.chat.dao;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;

public interface ChatroomMetadataRepository extends JpaRepository<ChatroomMetadata, Long> {

	Slice<ChatroomMetadata> findAllByUserIdOrderByLastChatTimeDesc(Long userId, Pageable pageable);

	Optional<ChatroomMetadata> findByChatroomIdAndUserId(Long chatroomId, Long senderId);

	void deleteAllByChatroomId(Long chatroomId);
}