package com.cotato.kampus.domain.chat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;

public interface ChatroomMetadataRepository extends JpaRepository<ChatroomMetadata, Long> {
	List<ChatroomMetadata> findAllByChatroomId(Long chatroomId);
}