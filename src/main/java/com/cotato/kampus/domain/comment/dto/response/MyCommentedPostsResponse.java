package com.cotato.kampus.domain.comment.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

import com.cotato.kampus.domain.post.dto.PostWithPhotos;

public record MyCommentedPostsResponse(
	List<PostWithPhotos> posts,
	boolean hasNext
) {
	public static MyCommentedPostsResponse from(Slice<PostWithPhotos> postWithPhotosSlice) {
		return new MyCommentedPostsResponse(
			postWithPhotosSlice.getContent(),
			postWithPhotosSlice.hasNext()
		);
	}
}
