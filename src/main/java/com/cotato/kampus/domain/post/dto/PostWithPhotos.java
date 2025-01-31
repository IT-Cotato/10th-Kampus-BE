package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.enums.PostCategory;

public record PostWithPhotos(
	Long id,
	PostCategory postCategory,
	String title,
	String content,
	Long likes,
	Long comments,
	LocalDateTime createdTime,
	String thumbnailUrl) {

	public static PostWithPhotos from(Post post, PostPhoto postPhoto) {
		return new PostWithPhotos(
			post.getId(),
			post.getPostCategory(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			post.getCreatedTime(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}