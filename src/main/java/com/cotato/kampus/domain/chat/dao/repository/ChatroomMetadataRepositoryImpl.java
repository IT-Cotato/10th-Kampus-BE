package com.cotato.kampus.domain.chat.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.chat.dao.entity.ChatroomMetadataEntity;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
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
	public Slice<ChatroomMetadata> findAllByUserIdOrderByLastChatTimeDesc(Long userId, Pageable pageable) {
		Slice<ChatroomMetadataEntity> entitySlice = chatroomMetadataJpaRepository.findAllByUserIdOrderByLastChatTimeDesc(
			userId, pageable);
		List<ChatroomMetadata> list = entitySlice.getContent().stream().map(ChatroomMetadataEntity::toDomain).toList();
		return new SliceImpl<>(list, entitySlice.getPageable(), entitySlice.hasNext());
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
}
