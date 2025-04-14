package com.cotato.kampus.domain.post.domain;

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