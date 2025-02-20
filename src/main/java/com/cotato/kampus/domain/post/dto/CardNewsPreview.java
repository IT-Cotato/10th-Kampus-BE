package com.cotato.kampus.domain.post.dto;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;

public record CardNewsPreview(
	Long postId,
	String title,
	String content,
	Long likes,
	Long comments,
	String thumbnailUrl,
	Boolean isScrapped
) {
	public static CardNewsPreview from(Post post, PostPhoto postPhoto, Boolean isScrapped) {
		return new CardNewsPreview(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null,
			isScrapped
		);
	}
}
