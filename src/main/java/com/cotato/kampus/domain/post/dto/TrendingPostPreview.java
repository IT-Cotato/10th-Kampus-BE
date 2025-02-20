package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;

public record TrendingPostPreview(
	Long postId,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	String thumbnailUrl
) {
	public static TrendingPostPreview from(Post post, String boardName, PostPhoto postPhoto) {
		return new TrendingPostPreview(
			post.getId(),
			boardName,
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}
