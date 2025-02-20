package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;

public record TrendingPostPreview(
	Long id,
	Long boardId,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	String thumbnailUrl
) {
	public static TrendingPostPreview from(Post post, Long boardId, String boardName, PostPhoto postPhoto) {
		return new TrendingPostPreview(
			post.getId(),
			boardId,
			boardName,
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}
