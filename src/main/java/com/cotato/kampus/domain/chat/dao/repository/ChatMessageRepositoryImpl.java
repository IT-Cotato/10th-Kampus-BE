package com.cotato.kampus.domain.chat.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessageEntity;
import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.implement.message.port.ChatMessageRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

	private final ChatMessageJpaRepository chatMessageJpaRepository;

	@Override
	public Slice<ChatMessage> findAllByChatroomIdOrderByCreatedTimeDesc(Long chatRoomId, PageRequest pageRequest) {
		Slice<ChatMessageEntity> entitySlice = chatMessageJpaRepository.findAllByChatroomIdOrderByCreatedTimeDesc(
			chatRoomId, pageRequest);

		List<ChatMessage> content = entitySlice.getContent().stream()
			.map(ChatMessageEntity::toDomain)
			.toList();

		return new SliceImpl<>(content, entitySlice.getPageable(), entitySlice.hasNext());
	}

	@Override
	public Optional<ChatMessage> findFirstByChatroomIdOrderByCreatedTimeDesc(Long chatroomId) {
		return chatMessageJpaRepository.findFirstByChatroomIdOrderByCreatedTimeDesc(chatroomId)
			.map(ChatMessageEntity::toDomain);
	}

	@Override
	public Long countByChatroomIdAndIdGreaterThan(Long chatroomId, Long messageId) {
		return chatMessageJpaRepository.countByChatroomIdAndIdGreaterThan(chatroomId, messageId);
	}

	@Override
	public void deleteAllByChatroomId(Long chatroomId) {
		chatMessageJpaRepository.deleteAllByChatroomId(chatroomId);
	}
}