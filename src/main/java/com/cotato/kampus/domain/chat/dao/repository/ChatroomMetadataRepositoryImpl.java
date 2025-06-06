package com.cotato.kampus.domain.chat.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ChatroomMetadataRepositoryImpl implements ChatroomMetadataRepository {

	private final ChatroomMetadataJpaRepository chatroomMetadataJpaRepository;

	@Override
	public void save(ChatroomMetadata senderMetadata) {
		chatroomMetadataJpaRepository.save(
			ChatroomMetadataEntity.fromDomain(senderMetadata));
	}

	@Override
	public void saveAll(List<ChatroomMetadata> chatroomMetadata) {
		chatroomMetadataJpaRepository.saveAll(
			chatroomMetadata.stream().map(ChatroomMetadataEntity::fromDomain).toList());
	}

	@Override
	public Slice<ChatroomMetadata> findAllByUserIdAndChatType(Long userId, ChatType chatType,
		Pageable pageable) {
		Slice<ChatroomMetadataEntity> entitySlice = chatroomMetadataJpaRepository.findAllByUserIdAndChatType(
			userId, chatType, pageable);
		return entitySlice.map(ChatroomMetadataEntity::toDomain);
	}

	@Override
	public Optional<ChatroomMetadata> findByChatroomIdAndUserId(Long chatroomId, Long senderId) {
		return chatroomMetadataJpaRepository.findByChatroomIdAndUserId(chatroomId, senderId)
			.map(ChatroomMetadataEntity::toDomain);
	}

	@Override
	public void deleteAllByChatroomId(Long chatroomId) {
		chatroomMetadataJpaRepository.deleteAllByChatroomId(chatroomId);
	}

	@Override
	public Slice<ChatroomMetadata> findAllByUserId(Long userId, Pageable pageable) {
		Slice<ChatroomMetadataEntity> entitySlice = chatroomMetadataJpaRepository.findAllByUserId(
			userId, pageable);
		return entitySlice.map(ChatroomMetadataEntity::toDomain);
	}
}
