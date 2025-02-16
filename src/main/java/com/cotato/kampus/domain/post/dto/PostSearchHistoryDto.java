package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.PostSearchHistory;

public record PostSearchHistoryDto(
	Long id,
	String keyword
) {
	public static PostSearchHistoryDto from(PostSearchHistory postSearchHistory) {
		return new PostSearchHistoryDto(
			postSearchHistory.getId(),
			postSearchHistory.getKeyword()
		);
	}
}