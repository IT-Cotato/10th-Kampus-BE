package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record MyPostWithPhoto(
	Long id,
	String boardName,
	String title,
	String content,
	Long likes,
	Long comments,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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