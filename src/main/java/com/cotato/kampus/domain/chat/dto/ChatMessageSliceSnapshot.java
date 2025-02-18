package com.cotato.kampus.domain.chat.dto;

import java.util.List;

public record ChatMessageSliceSnapshot(
	List<ChatMessageSnapshot> messages,
	boolean hasNext
) {
}