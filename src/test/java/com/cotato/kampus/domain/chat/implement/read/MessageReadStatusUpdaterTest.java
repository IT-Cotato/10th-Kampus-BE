package com.cotato.kampus.domain.chat.implement.read;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.MessageReadStatus;
import com.cotato.kampus.domain.chat.implement.read.port.MessageReadStatusRepository;

@ExtendWith(MockitoExtension.class)
class MessageReadStatusUpdaterTest {

	@InjectMocks
	private MessageReadStatusUpdater target;

	@Mock
	private MessageReadStatusRepository messageReadStatusRepository;

	@Test
	@DisplayName("읽음 상태가 없을 때 새로운 읽음 상태를 생성")
	void createNewStatusWhenNotExists() {
		// given
		Long chatroomId = 1L;
		Long userId = 2L;
		Long latestMessageId = 100L;

		// 기존 읽음 상태가 없는 상황 모킹
		when(messageReadStatusRepository.findByChatroomIdAndUserId(chatroomId, userId))
			.thenReturn(Optional.empty());

		// 저장되는 객체를 캡처하기 위한 ArgumentCaptor 설정
		ArgumentCaptor<MessageReadStatus> statusCaptor = ArgumentCaptor.forClass(MessageReadStatus.class);

		// when
		target.updateStatus(chatroomId, userId, latestMessageId);

		// then
		verify(messageReadStatusRepository).findByChatroomIdAndUserId(chatroomId, userId);
		verify(messageReadStatusRepository).save(statusCaptor.capture());

		MessageReadStatus capturedStatus = statusCaptor.getValue();
		assertThat(capturedStatus).isNotNull();
		assertThat(capturedStatus.getChatroomId()).isEqualTo(chatroomId);
		assertThat(capturedStatus.getUserId()).isEqualTo(userId);
		assertThat(capturedStatus.getLastReadMessageId()).isEqualTo(latestMessageId);
	}

	@Test
	@DisplayName("읽음 상태가 이미 있을 때 기존 읽음 상태를 업데이트")
	void updateExistingStatus() {
		// given
		Long chatroomId = 1L;
		Long userId = 2L;
		Long oldMessageId = 50L;
		Long newMessageId = 100L;

		// 실제 MessageReadStatus 객체 생성
		MessageReadStatus existingStatus = MessageReadStatus.builder()
			.chatroomId(chatroomId)
			.userId(userId)
			.lastReadMessageId(oldMessageId)
			.build();

		// 기존 읽음 상태가 있는 상황 모킹
		when(messageReadStatusRepository.findByChatroomIdAndUserId(chatroomId, userId))
			.thenReturn(Optional.of(existingStatus));

		// save 메서드가 호출될 때 전달된 객체를 그대로 반환하도록 설정
		doAnswer(invocation -> invocation.getArgument(0))
			.when(messageReadStatusRepository).save(any(MessageReadStatus.class));

		// when
		target.updateStatus(chatroomId, userId, newMessageId);

		// then
		// 리포지토리 메서드 호출 검증
		verify(messageReadStatusRepository).findByChatroomIdAndUserId(chatroomId, userId);
		verify(messageReadStatusRepository).save(existingStatus);

		assertThat(existingStatus).isNotNull();
		assertThat(existingStatus.getChatroomId()).isEqualTo(chatroomId);
		assertThat(existingStatus.getUserId()).isEqualTo(userId);
		assertThat(existingStatus.getLastReadMessageId())
			.isNotEqualTo(oldMessageId)  // 이전 값과 달라야 함
			.isEqualTo(newMessageId);    // 새로운 값으로 업데이트되어야 함
	}
}