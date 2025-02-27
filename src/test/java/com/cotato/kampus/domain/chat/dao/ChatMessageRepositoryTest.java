package com.cotato.kampus.domain.chat.dao;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.global.common.dto.CustomPageRequest;

@DataJpaTest
@ActiveProfiles("test")
class ChatMessageRepositoryTest {

	@Autowired
	ChatMessageRepository chatMessageRepository;

	private static final int PAGE_SIZE = 20;
	private static final String SORT_PROPERTY = "createdTime";

	@Test
	@DisplayName("채팅 메시지 최신순 조회_성공")
	public void 채팅_메시지_최신순_조회_성공() {
		// given
		ChatMessage chatMessage1 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("message")
			.build();
		ChatMessage chatMessage2 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("message")
			.build();
		ChatMessage chatMessage3 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("message")
			.build();
		List<ChatMessage> chatMessages = List.of(chatMessage1, chatMessage2, chatMessage3);
		chatMessageRepository.saveAll(chatMessages);
		CustomPageRequest customPageRequest = new CustomPageRequest(1, PAGE_SIZE, Sort.Direction.DESC);

		// when
		Slice<ChatMessage> slice = chatMessageRepository.findAllByChatroomIdOrderByCreatedTimeDesc(
			1L, customPageRequest.of(SORT_PROPERTY));

		// then
		Assertions.assertThat(slice.getContent().size()).isEqualTo(3);
		Assertions.assertThat(slice.getSize()).isEqualTo(PAGE_SIZE); // 페이지 크기는 20
	}
}