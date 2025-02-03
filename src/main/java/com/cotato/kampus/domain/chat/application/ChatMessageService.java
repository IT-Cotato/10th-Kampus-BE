package com.cotato.kampus.domain.chat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cotato.kampus.domain.chat.domain.ChatMessage;
import com.cotato.kampus.domain.chat.dto.ChatMessageSlice;
import com.cotato.kampus.domain.chat.dto.ChatMessageSliceWithUserId;
import com.cotato.kampus.domain.common.application.ApiUserResolver;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageService {

	private final ChatMessageFinder chatMessageFinder;
	private final ChatMessageAppender chatMessageAppender;
	private final ApiUserResolver apiUserResolver;
	private final ChatRoomValidator chatRoomValidator;

	@Transactional
	public ChatMessage saveMessage(Long chatRoomId, String content) {
		Long senderId = apiUserResolver.getUserId();
		return chatMessageAppender.appendChatMessage(senderId, chatRoomId, content);
	}

	public ChatMessageSliceWithUserId getMessages(int page, Long chatroomId) {
		// 1. 조회한 유저 id 조회
		Long userId = apiUserResolver.getUserId();

		// 2. 조회한 유저가 채팅방에 있는 유저인지 조회
		chatRoomValidator.validateUser(userId, chatroomId);

		// 3. Slice로 채팅 메시지 조회하여 현재 유저 id와 함께 반환
		ChatMessageSlice chatMessageSlice = chatMessageFinder.findAllByChatRoomId(page, chatroomId);
		return ChatMessageSliceWithUserId.from(userId, chatMessageSlice);
	}
}