package com.cotato.kampus.domain.post.api.response;

import com.cotato.kampus.domain.post.domain.PostSearchHistoryDto;

public record SearchKeywordResponse(
	Long id,
	String keyword
) {
	public static SearchKeywordResponse from(PostSearchHistoryDto searchHistoryDto) {
		return new SearchKeywordResponse(searchHistoryDto.id(), searchHistoryDto.keyword());
	}
}