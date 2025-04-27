package com.cotato.kampus.domain.chat.implement.metadata;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatroomMetadata;
import com.cotato.kampus.domain.chat.implement.metadata.port.ChatroomMetadataRepository;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;

@ExtendWith(MockitoExtension.class)
class ChatroomMetadataUpdaterTest {

	@InjectMocks
	private ChatroomMetadataUpdater target;

	@Mock
	private ChatroomMetadataRepository chatroomMetadataRepository;

	@Test
	@DisplayName("발신자 메타데이터 업데이트 성공")
	void updateSenderMetadata_Success() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;
		Long messageId = 100L;
		boolean isImage = false;
		String content = "Hello, World!";
		LocalDateTime createdTime = LocalDateTime.now();

		// 실제 객체 생성
		ChatMessage chatMessage = ChatMessage.builder()
			.id(messageId)
			.isImage(isImage)
			.content(content)
			.createdTime(createdTime)
			.build();

		// 실제 메타데이터 객체 생성
		ChatroomMetadata senderMetadata = ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.userId(senderId)
			.lastMessageId(50L) // 이전 메시지 ID
			.lastMessageContent("이전 메시지")
			.lastChatTime(LocalDateTime.now().minusHours(1))
			.unreadCount(0L)
			.build();

		// 리포지토리 모킹
		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, senderId))
			.thenReturn(Optional.of(senderMetadata));

		doAnswer(invocation -> invocation.getArgument(0))
			.when(chatroomMetadataRepository).save(any(ChatroomMetadata.class));

		// when
		target.updateSenderMetadata(chatroomId, chatMessage, senderId);

		// then
		// 상태 검증
		assertThat(senderMetadata.getChatroomId()).isEqualTo(chatroomId);
		assertThat(senderMetadata.getUserId()).isEqualTo(senderId);
		assertThat(senderMetadata.getLastMessageId()).isEqualTo(messageId); // 업데이트된 메시지 ID
		assertThat(senderMetadata.getLastMessageContent()).isEqualTo(content); // 업데이트된 콘텐츠
		assertThat(senderMetadata.isLastMessageImage()).isEqualTo(isImage); // 이미지 여부
		assertThat(senderMetadata.getLastChatTime()).isEqualTo(createdTime); // 업데이트된 시간
		assertThat(senderMetadata.getUnreadCount()).isZero(); // 발신자는 카운트 변경 없음

		// 리포지토리 호출 검증
		verify(chatroomMetadataRepository).findByChatroomIdAndUserId(chatroomId, senderId);
		verify(chatroomMetadataRepository).save(senderMetadata);
	}

	@Test
	@DisplayName("발신자 메타데이터 업데이트 실패 - 메타데이터 없음")
	void updateSenderMetadata_Failure_NotFound() {
		// given
		Long chatroomId = 1L;
		Long senderId = 2L;

		ChatMessage chatMessage = mock(ChatMessage.class);

		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, senderId))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> target.updateSenderMetadata(chatroomId, chatMessage, senderId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_METADATA_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("수신자 메타데이터 업데이트 성공")
	void updateReceiverMetadata_Success() {
		// given
		Long chatroomId = 1L;
		Long receiverId = 3L;
		Long messageId = 100L;
		boolean isImage = false;
		String content = "Hello, World!";
		LocalDateTime createdTime = LocalDateTime.now();
		Long initialUnreadCount = 2L; // 초기 읽지 않은 메시지 수

		// 실제 객체 생성
		ChatMessage chatMessage = ChatMessage.builder()
			.id(messageId)
			.isImage(isImage)
			.content(content)
			.createdTime(createdTime)
			.build();

		// 실제 메타데이터 객체 생성
		ChatroomMetadata receiverMetadata = ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.userId(receiverId)
			.lastMessageId(50L) // 이전 메시지 ID
			.lastMessageContent("이전 메시지")
			.lastChatTime(LocalDateTime.now().minusHours(1))
			.unreadCount(initialUnreadCount)
			.build();

		// 리포지토리 모킹
		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, receiverId))
			.thenReturn(Optional.of(receiverMetadata));

		doAnswer(invocation -> invocation.getArgument(0))
			.when(chatroomMetadataRepository).save(any(ChatroomMetadata.class));

		// when
		ChatroomMetadata result = target.updateReceiverMetadata(chatroomId, chatMessage, receiverId);

		// then
		// 상태 검증
		assertThat(receiverMetadata.getChatroomId()).isEqualTo(chatroomId);
		assertThat(receiverMetadata.getUserId()).isEqualTo(receiverId);
		assertThat(receiverMetadata.getLastMessageId()).isEqualTo(messageId);
		assertThat(receiverMetadata.getLastMessageContent()).isEqualTo(content);
		assertThat(receiverMetadata.isLastMessageImage()).isEqualTo(isImage);
		assertThat(receiverMetadata.getLastChatTime()).isEqualTo(createdTime);
		assertThat(receiverMetadata.getUnreadCount()).isEqualTo(initialUnreadCount + 1L);

		// 반환값 검증
		assertThat(result).isSameAs(receiverMetadata);

		// 리포지토리 호출 검증
		verify(chatroomMetadataRepository).findByChatroomIdAndUserId(chatroomId, receiverId);
		verify(chatroomMetadataRepository).save(receiverMetadata);
	}

	@Test
	@DisplayName("수신자 메타데이터 업데이트 실패 - 메타데이터 없음")
	void updateReceiverMetadata_Failure_NotFound() {
		// given
		Long chatroomId = 1L;
		Long receiverId = 3L;

		ChatMessage chatMessage = mock(ChatMessage.class);

		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, receiverId))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> target.updateReceiverMetadata(chatroomId, chatMessage, receiverId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_METADATA_NOT_FOUND.getMessage());
	}

	@Test
	@DisplayName("읽음 카운트 리셋 성공")
	void resetReadCount_Success() {
		// given
		Long chatroomId = 1L;
		Long userId = 2L;
		Long initialUnreadCount = 5L; // 초기 읽지 않은 메시지 수

		// 실제 메타데이터 객체 생성
		ChatroomMetadata metadata = ChatroomMetadata.builder()
			.chatroomId(chatroomId)
			.userId(userId)
			.lastMessageId(100L)
			.lastMessageContent("메시지 내용")
			.lastChatTime(LocalDateTime.now())
			.unreadCount(initialUnreadCount) // 초기 읽지 않은 메시지 수 설정
			.build();

		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, userId))
			.thenReturn(Optional.of(metadata));

		doAnswer(invocation -> invocation.getArgument(0))
			.when(chatroomMetadataRepository).save(any(ChatroomMetadata.class));

		// when
		target.resetReadCount(chatroomId, userId);

		// then
		// 상태 검증
		assertThat(metadata.getChatroomId()).isEqualTo(chatroomId);
		assertThat(metadata.getUserId()).isEqualTo(userId);
		assertThat(metadata.getUnreadCount()).isZero(); // 읽지 않은 메시지 수가 0으로 리셋됨

		// 다른 필드는 변경되지 않았는지 확인
		assertThat(metadata.getLastMessageId()).isEqualTo(100L);
		assertThat(metadata.getLastMessageContent()).isEqualTo("메시지 내용");

		// 리포지토리 호출 검증
		verify(chatroomMetadataRepository).findByChatroomIdAndUserId(chatroomId, userId);
		verify(chatroomMetadataRepository).save(metadata);
	}

	@Test
	@DisplayName("읽음 카운트 리셋 실패 - 메타데이터 없음")
	void resetReadCount_Failure_NotFound() {
		// given
		Long chatroomId = 1L;
		Long userId = 2L;

		when(chatroomMetadataRepository.findByChatroomIdAndUserId(chatroomId, userId))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> target.resetReadCount(chatroomId, userId))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.CHATROOM_METADATA_NOT_FOUND.getMessage());
	}
}