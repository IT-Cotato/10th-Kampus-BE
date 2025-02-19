package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.cotato.kampus.domain.post.enums.PostCategory;
import com.fasterxml.jackson.annotation.JsonFormat;

public record PostWithPhotos(
	Long id,
	PostCategory postCategory,
	String title,
	String content,
	Long likes,
	Long comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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