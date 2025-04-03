package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatroomMetadata {

	private Long id;
	private Long chatroomId;
	private Long userId;
	private Long postId;
	private String postTitle;
	private Long lastMessageId;
	private String lastMessageContent;
	private LocalDateTime lastChatTime;
	private Long unreadCount;
}
