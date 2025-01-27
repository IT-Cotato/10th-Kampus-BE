package com.cotato.kampus.domain.post.dto.response;

public record PostDraftResponse(Long postId) {
	public static PostDraftResponse of(Long id) {
		return new PostDraftResponse(id);
	}
}
