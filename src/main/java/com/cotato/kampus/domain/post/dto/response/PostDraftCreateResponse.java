package com.cotato.kampus.domain.post.dto.response;

public record PostDraftCreateResponse(Long postId) {
	public static PostDraftCreateResponse of(Long id) {
		return new PostDraftCreateResponse(id);
	}
}
