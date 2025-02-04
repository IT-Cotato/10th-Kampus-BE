package com.cotato.kampus.domain.chat.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.chat.domain.Chatroom;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {

	boolean existsByPostIdAndInitialSenderId(Long postId, Long senderId);

	@Query("SELECT c FROM Chatroom c " +
		"WHERE c.initialSenderId = :userId OR c.initialReceiverId = :userId " +
		"ORDER BY c.createdTime DESC")
	Slice<Chatroom> findAllByUserIdOrderByCreatedTimeDesc(@Param("userId") Long userId, Pageable pageable);
}