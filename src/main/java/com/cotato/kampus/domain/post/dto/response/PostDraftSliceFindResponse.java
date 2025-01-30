package com.cotato.kampus.domain.post.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.PostDraftWithPhoto;

public record PostDraftSliceFindResponse(
	List<PostDraftWithPhoto> draftPosts,
	boolean hasNext
) {
	public static PostDraftSliceFindResponse from(Slice<PostDraftWithPhoto> draftPosts){
		return new PostDraftSliceFindResponse(
			draftPosts.getContent(),
			draftPosts.hasNext()
		);
	}
}
