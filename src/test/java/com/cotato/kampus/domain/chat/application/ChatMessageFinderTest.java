package com.cotato.kampus.domain.chat.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import com.cotato.kampus.domain.chat.dao.repository.ChatMessageRepository;
import com.cotato.kampus.domain.chat.dao.entity.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatMessageSlice;
import com.cotato.kampus.domain.chat.implement.message.ChatMessageFinder;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

@ExtendWith(MockitoExtension.class)
class ChatMessageFinderTest {

	@InjectMocks
	private ChatMessageFinder target;

	@Mock
	private ChatMessageRepository chatMessageRepository;

	private static final int PAGE_SIZE = 20;
	private static final String SORT_PROPERTY = "createdTime";

	@Test
	@DisplayName("채팅방 아이디로 채팅 메시지 조회_성공")
	public void 채팅방_아이디로_채팅_메시지_조회_성공() {
		// given

		CustomPageRequest customPageRequest = new CustomPageRequest(1, PAGE_SIZE, Sort.Direction.DESC);
		Slice<ChatMessage> slice = mock(Slice.class);
		given(chatMessageRepository.findAllByChatroomIdOrderByCreatedTimeDesc(
			1L,
			customPageRequest.of(SORT_PROPERTY)))
			.willReturn(slice);

		// when
		ChatMessageSlice chatMessageSlice = target.findAllByChatRoomId(1, 1L);

		// then
		assertThat(chatMessageSlice).isNotNull();
	}
}