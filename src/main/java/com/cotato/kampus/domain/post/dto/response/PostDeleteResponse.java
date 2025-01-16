package com.cotato.kampus.domain.post.dto.response;

public record PostDeleteResponse(Long id) {
	public static PostDeleteResponse of(Long id){
		return new PostDeleteResponse(id);
	}
}