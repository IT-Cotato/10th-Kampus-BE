package com.cotato.kampus.domain.chat.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.chat.dao.repository.ChatRoomJpaRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatRoomEntity;
import com.cotato.kampus.domain.chat.enums.ChatType;
import com.cotato.kampus.domain.chat.enums.InitiatedFrom;

@DataJpaTest
@ActiveProfiles("test")
class ChatRoomJpaRepositoryTest {

	@Autowired
	private ChatRoomJpaRepository chatRoomJpaRepository;

	@Test
	public void 채팅방_생성() {
		ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
			.referenceId(1L)
			.chatType(ChatType.POST)
			.initialSenderId(1L)
			.initialReceiverId(2L)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();

		ChatRoomEntity savedChatRoomEntity = chatRoomJpaRepository.save(chatRoomEntity);

		assertThat(savedChatRoomEntity).isNotNull();
		assertThat(savedChatRoomEntity.getId()).isGreaterThan(0L);
	}

	@Test
	public void PostId와_InitialSenderId로_채팅방_존재_여부_확인() {
		ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
			.referenceId(1L)
			.chatType(ChatType.POST)
			.initialSenderId(1L)
			.initialReceiverId(2L)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();

		chatRoomJpaRepository.save(chatRoomEntity);

		boolean exists = chatRoomJpaRepository.existsByReferenceIdAndInitialSenderId(1L, 1L);

		assertThat(exists).isTrue();
	}

	@Test
	public void chatroomId로_채팅방_조회() {
		ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
			.referenceId(1L)
			.chatType(ChatType.POST)
			.initialSenderId(1L)
			.initialReceiverId(2L)
			.isBlocked(false)
			.initiatedFrom(InitiatedFrom.POST)
			.build();

		ChatRoomEntity savedChatRoomEntity = chatRoomJpaRepository.save(chatRoomEntity);

		ChatRoomEntity findChatRoomEntity = chatRoomJpaRepository.findById(savedChatRoomEntity.getId())
			.orElse(null);

		assertThat(findChatRoomEntity).isNotNull();
		assertThat(findChatRoomEntity.getId()).isEqualTo(savedChatRoomEntity.getId());
	}
}