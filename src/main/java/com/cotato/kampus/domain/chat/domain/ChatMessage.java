package com.cotato.kampus.domain.chat.domain;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.chat.enums.ChatType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessage {

	private Long id;
	private Long chatroomId;
	private ChatType chatType;
	private Long senderId;
	private String content;
	private boolean isImage;
	private LocalDateTime createdTime;
	private LocalDateTime lastModifiedTime;
}