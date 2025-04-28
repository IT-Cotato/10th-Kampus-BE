package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

	private Long id;
	private Long chatroomId;
	private Long senderId;
	private String content;
	private boolean isImage;
	private LocalDateTime createdTime;
	private LocalDateTime lastModifiedTime;
}