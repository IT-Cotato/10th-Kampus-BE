package com.cotato.kampus.domain.post.api.response;

public record SearchKeywordDeleteResponse(
	Long id
) {
	public static SearchKeywordDeleteResponse from(Long id) {
		return new SearchKeywordDeleteResponse(id);
	}
}