package com.cotato.kampus.domain.chat.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.domain.Chatroom;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {

	boolean existsByPostIdAndInitialSenderId(Long postId, Long senderId);
}