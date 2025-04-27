package com.cotato.kampus.domain.post.api.response;

public record TempPostCreateResponse(Long postDraftId) {
	public static TempPostCreateResponse of(Long postDraftId) {
		return new TempPostCreateResponse(postDraftId);
	}
}
