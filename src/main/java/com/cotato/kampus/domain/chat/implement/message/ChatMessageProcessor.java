package com.cotato.kampus.domain.chat.implement.message;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.cotato.kampus.domain.chat.dao.entity.ChatMessage;
import com.cotato.kampus.domain.chat.domain.ChatMessageSlice;
import com.cotato.kampus.domain.chat.domain.ChatMessageSliceSnapshot;
import com.cotato.kampus.domain.chat.domain.ChatMessageSnapshot;
import com.cotato.kampus.domain.chat.domain.MessageReadStatusDto;
import com.cotato.kampus.domain.chat.implement.read.MessageReadStatusFinder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageProcessor {
	private final MessageReadStatusFinder messageReadStatusFinder;

	public ChatMessageSliceSnapshot attachReadStatus(
		ChatMessageSlice chatMessageSlice,
		Long chatroomId,
		Long userId
	) {
		// 읽음 상태 가져옴
		Optional<MessageReadStatusDto> readStatus = messageReadStatusFinder.findByChatroomIdAndUserId(
			chatroomId, userId);

		// 읽음 상태 이후의 메시지들에 대해 읽음 상태를 false로 설정
		List<ChatMessageSnapshot> processedMessages = chatMessageSlice.chatMessages().stream()
			.map(chatMessage -> new ChatMessageSnapshot(
				chatMessage.getId(),
				chatMessage.getChatroomId(),
				chatMessage.getSenderId(),
				chatMessage.getContent(),
				readStatus.filter(messageReadStatusDto -> determineReadStatus(chatMessage, messageReadStatusDto))
					.isPresent(),
				chatMessage.getSenderId().equals(userId)
			))
			.toList();

		return new ChatMessageSliceSnapshot(processedMessages, chatMessageSlice.chatMessages().hasNext());
	}

	private boolean determineReadStatus(ChatMessage message, @NotNull MessageReadStatusDto readStatus) {
		return message.getId() <= readStatus.lastReadMessageId();
	}
}