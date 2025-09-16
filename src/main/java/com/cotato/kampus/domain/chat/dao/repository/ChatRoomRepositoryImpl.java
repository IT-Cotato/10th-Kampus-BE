package com.cotato.kampus.domain.chat.dao.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.chat.domain.ChatRoom;
import com.cotato.kampus.domain.chat.enums.ChatType;
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
	public boolean existsByReferenceIdAndInitialSenderIdAndChatType(Long referenceId, Long senderId, ChatType chatType) {
		return chatRoomJpaRepository.existsByReferenceIdAndInitialSenderIdAndChatType(referenceId, senderId, chatType);
	}

	@Override
	public Slice<ChatRoom> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable) {
		return chatRoomJpaRepository.findAllByUserIdOrderByCreatedTimeDesc(userId, pageable)
			.map(ChatRoomEntity::toDomain);
	}

	@Override
	public Optional<ChatRoom> findByReferenceIdAndInitialSenderIdAndChatType(Long referenceId, Long initialSenderId,
		ChatType chatType) {
		return chatRoomJpaRepository.findByReferenceIdAndInitialSenderIdAndChatType(referenceId, initialSenderId, chatType)
			.map(ChatRoomEntity::toDomain);
	}
}