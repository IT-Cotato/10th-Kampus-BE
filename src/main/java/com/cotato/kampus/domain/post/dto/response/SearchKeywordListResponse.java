package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import com.cotato.kampus.domain.post.dto.PostSearchHistoryList;

public record SearchKeywordListResponse(
	List<SearchKeywordResponse> keywords
) {
	public static SearchKeywordListResponse from(PostSearchHistoryList historyList) {
		return new SearchKeywordListResponse(
			historyList.postSearchHistories()
				.stream()
				.map(SearchKeywordResponse::from)
				.toList());
	}
}