package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.PostThumbnail;

public record PostSliceFindResponse(
	List<PostThumbnail> posts,
	Boolean hasNext
) {
	public static PostSliceFindResponse from(Slice<PostThumbnail> postSlice) {
		return new PostSliceFindResponse(
			postSlice.getContent(),
			postSlice.hasNext()
		);
	}
}