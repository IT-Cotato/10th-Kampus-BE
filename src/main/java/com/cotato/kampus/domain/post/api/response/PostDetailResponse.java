package com.cotato.kampus.domain.post.api.response;

import com.cotato.kampus.domain.post.domain.PostDetails;

public record PostDetailResponse(
	PostDetails postDetails
) {
	public static PostDetailResponse from(PostDetails postDetails) {
		return new PostDetailResponse(postDetails);
	}
}