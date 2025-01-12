package com.cotato.kampus.domain.post.dto.response;

public record PostCreateResponse(Long id) {
	public static PostCreateResponse of(Long id){
		return new PostCreateResponse(id);
	}
}
