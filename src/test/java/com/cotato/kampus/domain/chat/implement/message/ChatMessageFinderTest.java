package com.cotato.kampus.domain.chat.implement.message;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatMessageSlice;
import com.cotato.kampus.domain.chat.implement.message.port.ChatMessageRepository;

@ExtendWith(MockitoExtension.class)
class ChatMessageFinderTest {

	@InjectMocks
	private ChatMessageFinder target;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	@Test
	@DisplayName("채팅방 ID로 메시지 목록 조회 성공")
	void findAllByChatRoomId_Success() {
		// given
		int page = 1; // CustomPageRequest에서 1부터 시작
		Long chatRoomId = 1L;

		List<ChatMessage> messages = List.of(
			createChatMessage(3L, chatRoomId, "메시지3", LocalDateTime.now()),
			createChatMessage(2L, chatRoomId, "메시지2", LocalDateTime.now().minusMinutes(1)),
			createChatMessage(1L, chatRoomId, "메시지1", LocalDateTime.now().minusMinutes(2))
		);

		// PageRequest는 0부터 시작하므로 page-1로 변환
		PageRequest pageRequest = PageRequest.of(0, 20, Sort.Direction.DESC, "createdTime");
		SliceImpl<ChatMessage> slice = new SliceImpl<>(messages, pageRequest, false);

		when(chatMessageRepository.findAllByChatroomIdOrderByCreatedTimeDesc(eq(chatRoomId), any(PageRequest.class)))
			.thenReturn(slice);

		// when
		ChatMessageSlice result = target.findAllByChatRoomId(page, chatRoomId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.chatMessages().getContent()).hasSize(3);
		assertThat(result.chatMessages().hasNext()).isFalse();
		assertThat(result.chatMessages().getContent().get(0).getContent()).isEqualTo("메시지3");
		assertThat(result.chatMessages().getContent().get(1).getContent()).isEqualTo("메시지2");
		assertThat(result.chatMessages().getContent().get(2).getContent()).isEqualTo("메시지1");
	}

	@Test
	@DisplayName("채팅방 ID로 가장 최근 메시지 조회 성공")
	void findLatestMessage_Success() {
		// given
		Long chatRoomId = 1L;
		ChatMessage latestMessage = createChatMessage(3L, chatRoomId, "최근 메시지", LocalDateTime.now());

		when(chatMessageRepository.findFirstByChatroomIdOrderByCreatedTimeDesc(chatRoomId))
			.thenReturn(Optional.of(latestMessage));

		// when
		ChatMessage result = target.findLatestMessage(chatRoomId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(3L);
		assertThat(result.getContent()).isEqualTo("최근 메시지");
	}

	@Test
	@DisplayName("채팅방 ID로 가장 최근 메시지 조회 실패 - 메시지 없음")
	void findLatestMessage_Failure_NoMessage() {
		// given
		Long chatRoomId = 1L;

		when(chatMessageRepository.findFirstByChatroomIdOrderByCreatedTimeDesc(chatRoomId))
			.thenReturn(Optional.empty());

		// when
		ChatMessage result = target.findLatestMessage(chatRoomId);

		// then
		assertThat(result).isNull();
	}

	private ChatMessage createChatMessage(Long id, Long chatRoomId, String content, LocalDateTime createdTime) {
		return ChatMessage.builder()
			.id(id)
			.chatroomId(chatRoomId)
			.senderId(2L)
			.content(content)
			.createdTime(createdTime)
			.build();
	}
}