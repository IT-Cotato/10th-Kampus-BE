package com.cotato.kampus.domain.chat.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatReference {
	private final Long referenceId;
	private final Long referenceUserId;
	private final String title;
}