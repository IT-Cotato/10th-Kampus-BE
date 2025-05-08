package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;
import com.cotato.kampus.domain.chat.enums.ChatType;

public interface ChatroomMetadataJpaRepository extends JpaRepository<ChatroomMetadataEntity, Long> {

	Slice<ChatroomMetadataEntity> findAllByUserIdOrderByLastChatTimeDesc(Long userId, Pageable pageable);

	Slice<ChatroomMetadataEntity> findAllByUserIdAndChatTypeOrderByLastChatTimeDesc(Long userId, ChatType chatType,
		Pageable pageable);

	Optional<ChatroomMetadataEntity> findByChatroomIdAndUserId(Long chatroomId, Long senderId);

	void deleteAllByChatroomId(Long chatroomId);
}