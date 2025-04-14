package com.cotato.kampus.domain.post.api.response;

public record PostDraftCreateResponse(Long postDraftId) {
	public static PostDraftCreateResponse of(Long postDraftId) {
		return new PostDraftCreateResponse(postDraftId);
	}
}
