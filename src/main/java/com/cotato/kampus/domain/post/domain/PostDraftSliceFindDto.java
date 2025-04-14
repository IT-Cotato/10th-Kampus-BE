package com.cotato.kampus.domain.post.domain;

import java.util.List;

import org.springframework.data.domain.Slice;

public record PostDraftSliceFindDto(
	List<PostDraftWithPhoto> draftPosts,
	boolean hasNext,
	int totalCount
) {
	public static PostDraftSliceFindDto from(Slice<PostDraftWithPhoto> draftPosts, int totalCount) {
		return new PostDraftSliceFindDto(
			draftPosts.getContent(),
			draftPosts.hasNext(),
			totalCount
		);
	}
}
