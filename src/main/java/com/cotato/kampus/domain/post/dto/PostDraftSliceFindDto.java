package com.cotato.kampus.domain.post.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.response.PostDraftSliceFindResponse;

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
