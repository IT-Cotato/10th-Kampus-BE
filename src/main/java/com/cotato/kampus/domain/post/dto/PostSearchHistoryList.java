package com.cotato.kampus.domain.post.dto;

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