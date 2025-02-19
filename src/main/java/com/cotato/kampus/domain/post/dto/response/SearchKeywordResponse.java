package com.cotato.kampus.domain.post.dto.response;

import com.cotato.kampus.domain.post.dto.PostSearchHistoryDto;

public record SearchKeywordResponse(
	Long id,
	String keyword
) {
	public static SearchKeywordResponse from(PostSearchHistoryDto searchHistoryDto) {
		return new SearchKeywordResponse(searchHistoryDto.id(), searchHistoryDto.keyword());
	}
}