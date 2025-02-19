package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.SearchedPost;

public record SearchedPostResponse(
	List<SearchedPost> posts,
	Boolean hasNext
) {
	public static SearchedPostResponse from(Slice<SearchedPost> postSlice) {
		return new SearchedPostResponse(
			postSlice.getContent(),
			postSlice.hasNext()
		);
	}
}