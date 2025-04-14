package com.cotato.kampus.domain.post.domain;

import java.util.List;

public record PostSearchHistoryList(
	List<PostSearchHistoryDto> postSearchHistories
) {
	public static PostSearchHistoryList from(List<PostSearchHistoryDto> histories) {
		return new PostSearchHistoryList(
			histories
		);
	}
}