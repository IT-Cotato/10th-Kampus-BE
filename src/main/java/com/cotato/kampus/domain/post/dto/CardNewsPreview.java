package com.cotato.kampus.domain.post.dto;

import java.time.LocalDateTime;

import com.cotato.kampus.domain.post.domain.Post;
import com.cotato.kampus.domain.post.domain.PostPhoto;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CardNewsPreview(
	Long postId,
	String title,
	String content,
	Long likes,
	Long comments,
	String thumbnailUrl,
	Boolean isScrapped,
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createdTime
) {
	public static CardNewsPreview from(Post post, PostPhoto postPhoto, Boolean isScrapped) {
		return new CardNewsPreview(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			post.getLikes(),
			post.getComments(),
			postPhoto != null ? postPhoto.getPhotoUrl() : null,
			isScrapped,
			post.getCreatedTime()
		);
	}
}
