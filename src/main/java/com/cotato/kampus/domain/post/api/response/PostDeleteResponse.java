package com.cotato.kampus.domain.post.api.response;

public record PostDeleteResponse(Long id) {
	public static PostDeleteResponse of(Long id){
		return new PostDeleteResponse(id);
	}
}