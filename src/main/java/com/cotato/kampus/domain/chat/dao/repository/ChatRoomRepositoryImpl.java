package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.implement.chatroom.port.ChatRoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

	private final ChatRoomJpaRepository chatRoomJpaRepository;

	@Override
	public Long save(ChatRoom chatRoom) {
		return chatRoomJpaRepository.save(ChatRoomEntity.fromDomain(chatRoom)).getId();
	}

	@Override
	public Optional<ChatRoom> findById(Long chatroomId) {
		return chatRoomJpaRepository.findById(chatroomId).map(ChatRoomEntity::toDomain);
	}

	@Override
	public boolean existsByPostIdAndInitialSenderId(Long postId, Long senderId) {
		return chatRoomJpaRepository.existsByPostIdAndInitialSenderId(postId, senderId);
	}

	@Override
	public Slice<ChatRoom> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable) {
		return chatRoomJpaRepository.findAllByUserIdOrderByCreatedTimeDesc(userId, pageable)
			.map(ChatRoomEntity::toDomain);
	}
}