package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.PostDraftSliceFindDto;
import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;

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
