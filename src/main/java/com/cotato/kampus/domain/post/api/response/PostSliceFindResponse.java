package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.PostWithPhotos;

public record PostSliceFindResponse(
	List<PostWithPhotos> posts,
	Boolean hasNext
) {
	public static PostSliceFindResponse from(Slice<PostWithPhotos> postSlice) {
		return new PostSliceFindResponse(
			postSlice.getContent(),
			postSlice.hasNext()
		);
	}
}