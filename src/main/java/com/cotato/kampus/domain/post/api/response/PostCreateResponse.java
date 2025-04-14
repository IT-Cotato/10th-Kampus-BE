package com.cotato.kampus.domain.post.api.response;

public record PostCreateResponse(Long postId) {
	public static PostCreateResponse of(Long postId){
		return new PostCreateResponse(postId);
	}
}
