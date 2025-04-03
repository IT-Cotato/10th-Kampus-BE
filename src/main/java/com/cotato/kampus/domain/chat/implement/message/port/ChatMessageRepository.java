package com.cotato.kampus.domain.chat.implement.message.port;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import com.cotato.kampus.domain.chat.domain.ChatMessage;

public interface ChatMessageRepository {

	Slice<ChatMessage> findAllByChatroomIdOrderByCreatedTimeDesc(Long chatRoomId, PageRequest pageRequest);

	Optional<ChatMessage> findFirstByChatroomIdOrderByCreatedTimeDesc(Long chatroomId);

	Long countByChatroomIdAndIdGreaterThan(
		@Param("chatroomId") Long chatroomId,
		@Param("messageId") Long messageId
	);

	void deleteAllByChatroomId(Long chatroomId);
}