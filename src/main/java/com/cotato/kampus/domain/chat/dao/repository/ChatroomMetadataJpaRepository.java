package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;

public interface ChatroomMetadataJpaRepository extends JpaRepository<ChatroomMetadataEntity, Long> {

	Slice<ChatroomMetadataEntity> findAllByUserIdOrderByLastChatTimeDesc(Long userId, Pageable pageable);

	Optional<ChatroomMetadataEntity> findByChatroomIdAndUserId(Long chatroomId, Long senderId);

	void deleteAllByChatroomId(Long chatroomId);
}