package com.cotato.kampus.domain.chat.api.response;

import java.util.List;

public record ChatImageResponse(
	List<String> imageUrls
) {
	public static ChatImageResponse from(List<String> chatImages) {
		return new ChatImageResponse(chatImages);
	}
}