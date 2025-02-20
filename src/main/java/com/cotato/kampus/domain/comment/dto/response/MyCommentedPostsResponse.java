package com.cotato.kampus.domain.comment.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.PostPreview;

public record MyCommentedPostsResponse(
	List<PostPreview> posts,
	boolean hasNext
) {
	public static MyCommentedPostsResponse from(Slice<PostPreview> postPreviews) {
		return new MyCommentedPostsResponse(
			postPreviews.getContent(),
			postPreviews.hasNext()
		);
	}
}