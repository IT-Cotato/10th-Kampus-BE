package com.cotato.kampus.domain.post.domain;

public record PostSearchHistoryWithUserId(
	Long id,
	Long userId,
	String keyword
) {
	public static PostSearchHistoryWithUserId from(PostSearchHistory postSearchHistory) {
		return new PostSearchHistoryWithUserId(
			postSearchHistory.getId(),
			postSearchHistory.getUserId(),
			postSearchHistory.getKeyword());
	}
}