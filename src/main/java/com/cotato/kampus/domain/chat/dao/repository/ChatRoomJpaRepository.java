package com.cotato.kampus.domain.chat.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.chat.enums.ChatType;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {

	boolean existsByReferenceIdAndInitialSenderIdAndChatType(Long referenceId, Long senderId, ChatType chatType);

	@Query("SELECT c FROM ChatRoomEntity c " +
		"WHERE c.initialSenderId = :userId OR c.initialReceiverId = :userId " +
		"ORDER BY c.createdTime DESC")
	Slice<ChatRoomEntity> findAllByUserIdOrderByCreatedTimeDesc(@Param("userId") Long userId, Pageable pageable);

	int countByInitialReceiverIdAndReferenceIdAndChatType(Long initialReceiverId, Long referenceId, ChatType chatType);
}