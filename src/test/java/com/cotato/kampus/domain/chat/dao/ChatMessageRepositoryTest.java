package com.cotato.kampus.domain.chat.dao;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.global.common.dto.CustomPageRequest;
import com.cotato.kampus.global.config.JpaAuditingConfig;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
class ChatMessageRepositoryTest {

	@Autowired
	ChatMessageRepository chatMessageRepository;

	private static final int PAGE_SIZE = 20;
	private static final String SORT_PROPERTY = "createdTime";

	@Test
	@DisplayName("채팅 메시지 최신순 조회 성공")
	public void findChatMessages() throws InterruptedException {
		// given
		ChatMessage chatMessage1 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("첫 번째 메시지")
			.build();
		chatMessageRepository.save(chatMessage1);

		ChatMessage chatMessage2 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("두 번째 메시지")
			.build();
		chatMessageRepository.save(chatMessage2);

		ChatMessage chatMessage3 = ChatMessage.builder()
			.chatroomId(1L)
			.senderId(1L)
			.content("세 번째 메시지")
			.build();
		chatMessageRepository.save(chatMessage3);

		CustomPageRequest customPageRequest = new CustomPageRequest(1, PAGE_SIZE, Sort.Direction.DESC);

		// when
		Slice<ChatMessage> slice = chatMessageRepository.findAllByChatroomIdOrderByCreatedTimeDesc(
			1L, customPageRequest.of(SORT_PROPERTY));

		// then
		List<ChatMessage> messages = slice.getContent();
		Assertions.assertThat(messages.size()).isEqualTo(3);
		Assertions.assertThat(messages.get(0).getContent()).isEqualTo("세 번째 메시지");
		Assertions.assertThat(messages.get(1).getContent()).isEqualTo("두 번째 메시지");
		Assertions.assertThat(messages.get(2).getContent()).isEqualTo("첫 번째 메시지");
	}
}