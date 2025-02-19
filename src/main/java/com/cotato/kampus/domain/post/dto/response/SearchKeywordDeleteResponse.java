package com.cotato.kampus.domain.post.dto.response;

public record SearchKeywordDeleteResponse(
	Long id
) {
	public static SearchKeywordDeleteResponse from(Long id) {
		return new SearchKeywordDeleteResponse(id);
	}
}