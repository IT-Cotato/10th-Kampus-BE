package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.domain.TrendingPostPreview;

public record TrendingListResponse(
	List<TrendingPostPreview> posts,
	Boolean hasNext
) {
	public static TrendingListResponse from(Slice<TrendingPostPreview> posts) {
		return new TrendingListResponse(
			posts.getContent(),
			posts.hasNext()
		);
	}
}
