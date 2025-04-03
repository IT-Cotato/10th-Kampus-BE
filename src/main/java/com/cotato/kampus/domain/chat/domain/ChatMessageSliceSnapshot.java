package com.cotato.kampus.domain.chat.domain;

import java.util.List;

public record ChatMessageSliceSnapshot(
	List<ChatMessageSnapshot> messages,
	boolean hasNext
) {
}