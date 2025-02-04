package com.cotato.kampus.domain.chat.dao;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	Slice<ChatMessage> findAllByChatroomIdOrderByCreatedTimeDesc(Long chatRoomId, PageRequest pageRequest);

	Optional<ChatMessage> findFirstByChatroomIdOrderByCreatedTimeDesc(Long chatroomId);
}