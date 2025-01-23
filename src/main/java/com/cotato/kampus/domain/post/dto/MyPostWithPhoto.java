package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;

public record MyPostWithPhoto(
	Long id,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	LocalDateTime createdTime,
	String thumbnailUrl) {

	public static MyPostWithPhoto from(Post post, String boardName, PostPhoto postPhoto) {
		return new MyPostWithPhoto(
			post.getId(),
			boardName,
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			post.getCreatedTime(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null
		);
	}
}