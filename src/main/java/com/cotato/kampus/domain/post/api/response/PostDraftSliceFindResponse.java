package com.cotato.kampus.domain.post.api.response;

import java.util.List;

import com.cotato.kampus.domain.post.domain.PostDraftSliceFindDto;
import com.cotato.kampus.domain.post.domain.PostDraftWithPhoto;

public record PostDraftSliceFindResponse(
	List<PostDraftWithPhoto> draftPosts,
	boolean hasNext,
	int totalCount
) {
	public static PostDraftSliceFindResponse from(PostDraftSliceFindDto draftSliceFindDto){
		return new PostDraftSliceFindResponse(
			draftSliceFindDto.draftPosts(),
			draftSliceFindDto.hasNext(),
			draftSliceFindDto.totalCount()
		);
	}
}
